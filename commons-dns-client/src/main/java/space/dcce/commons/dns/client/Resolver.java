package space.dcce.commons.dns.client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.dcce.commons.dns.client.cache.DnsCache;
import space.dcce.commons.dns.client.cache.SimpleDnsCache;
import space.dcce.commons.dns.exceptions.DnsClientConnectException;
import space.dcce.commons.dns.exceptions.DnsNoRecordFoundException;
import space.dcce.commons.dns.exceptions.DnsResponseTimeoutException;
import space.dcce.commons.dns.messages.QuestionRecord;
import space.dcce.commons.dns.messages.RecordClass;
import space.dcce.commons.dns.records.ARecord;
import space.dcce.commons.dns.records.RecordType;
import space.dcce.commons.dns.records.ResourceRecord;
import space.dcce.commons.general.EventCounter;
import space.dcce.commons.general.FileUtils;
import space.dcce.commons.general.UnexpectedException;
import space.dcce.commons.netaddr.SimpleInetAddress;


public class Resolver
{
	private final static Logger logger = LoggerFactory.getLogger(Resolver.class);
//	private final DnsClientPool clientPool;
	private final EventCounter totalRequestCounter;

	private DnsCache cache = new SimpleDnsCache();
	private long responseTimeout = 10000;
	private boolean cacheNegativeResponses = true;
	private int maxTotalRequestsPerSecond = 100;
	private int maxRequestsPerServerPerSecond = 30;
	private static final String PUBLIC_DNS_SERVERS_FILE = "/public_dns_servers.txt";
	private final List<String> upstreamServers;
	private boolean padSecondRequest;
	private boolean roundRobin = true;
	private int roundRobinDuplicateCount;
	
	/**
	 * Attempt to use system DNS servers. Note that Java does not have a reliable way to determine this.
	 */
	public Resolver()
	{
		this(SystemDnsServers.getSystemDnsServers(), true);
	}


	/**
	 * 
	 * @param upstreamServers
	 * @param padSecondRequest Makes the second request a throw-away to accommodate Google, et al
	 */
	public Resolver(List<String> upstreamServers, boolean padSecondRequest) throws IllegalArgumentException
	{
		if (upstreamServers == null || upstreamServers.isEmpty())
		{
			throw new IllegalArgumentException("No server addresses provided.");
		}
		this.upstreamServers = upstreamServers;
		this.padSecondRequest = padSecondRequest;
		totalRequestCounter = new EventCounter(1000, true);
	}
	

	public static Resolver createPublicServersResolver()
	{
		URL url = Resolver.class.getResource(PUBLIC_DNS_SERVERS_FILE);
		List<String> servers = new ArrayList<String>(1);
		try
		{
			servers.addAll(FileUtils.readConfigFileLines(url));
		}
		catch (IOException e)
		{
			logger.warn("Failed to open list of publicd DNS servers. Using Google only.");
			servers.add("8.8.8.8");
			servers.add("8.8.4.4");
		}
		return new Resolver(servers, true);
	}


	public List<SimpleInetAddress> simpleResolve(String hostname, boolean useCache) throws DnsResponseTimeoutException, DnsClientConnectException,
			DnsNoRecordFoundException
	{
		List<SimpleInetAddress> addresses = new ArrayList<SimpleInetAddress>();
		QuestionRecord question = new QuestionRecord(hostname, RecordType.A, RecordClass.IN);
		List<ResourceRecord> answers = query(question, useCache, true);
		for (ResourceRecord answer : answers)
		{
			if (answer instanceof ARecord)
			{
				ARecord a = (ARecord) answer;
				addresses.add(a.getAddress());
			}
		}
		return addresses;
	}


	public List<ResourceRecord> query(QuestionRecord question, boolean useCache, boolean recurse) throws DnsClientConnectException,
			DnsResponseTimeoutException, DnsNoRecordFoundException
	{
		DnsTransaction transaction = new DnsTransaction(question, recurse);
		bulkQuery(Collections.singletonList(transaction), useCache, true);

		if (transaction.isNegativeResponse())
			throw new DnsNoRecordFoundException();
		
		if (transaction.hasAnswer())
			return transaction.getAnswers();
		
		throw new UnexpectedException("This shouldn't be possible");
	}


	/**
	 * If blocking, returns number of unanswered queries
	 * @param transactions
	 * @param useCache
	 * @param recurse
	 * @param block
	 * @return
	 * @throws DnsClientConnectException
	 * @throws DnsResponseTimeoutException
	 */
	public int bulkQuery(List<DnsTransaction> transactions, boolean useCache, boolean block) throws DnsClientConnectException, DnsResponseTimeoutException
	{
		logger.trace("Starting Resolver.bulkQuery");
		List<DnsTransaction> unansweredTransactions = new ArrayList<DnsTransaction>(transactions);
		
		DnsClientPool clientPool = new DnsClientPool(upstreamServers, padSecondRequest, maxRequestsPerServerPerSecond, roundRobin, roundRobinDuplicateCount);
		try
		{
			for (DnsTransaction transaction : transactions)
			{
				QuestionRecord question = transaction.getQuestion();
				logger.trace("Processing DNS question for " + question.toString());
				if (useCache && cache.contains(question))
				{
					logger.trace("Answer found in cache.");
					transaction.addAnswers(cache.get(question));
					unansweredTransactions.remove(transaction);
					continue;
				}
				// Tell the transaction about the cache so it can be recorded when the response comes in
				transaction.setCache(cache);
				
				for (DnsClient client: clientPool.chooseNextClients())
				{
					try
					{
						totalRequestCounter.waitForThrottle(maxTotalRequestsPerSecond);
					}
					catch (InterruptedException e)
					{
						clientPool.close();
						Thread.currentThread().interrupt();
					}
					totalRequestCounter.trackEvent();
					logger.trace("Sending question to client");
					client.sendQuery(transaction);
				}
			}
	
			if (block)
			{
				long lastQueryTime = System.currentTimeMillis();
				
				while (System.currentTimeMillis() - lastQueryTime < responseTimeout && !unansweredTransactions.isEmpty())
				{
					for (Iterator<DnsTransaction> iterator = unansweredTransactions.iterator(); iterator.hasNext();)
					{
						DnsTransaction transaction = iterator.next();
						if(transaction.hasAnswer() || transaction.getNegativeResponseCount() >= roundRobinDuplicateCount)
							iterator.remove();
					}
					synchronized(unansweredTransactions)
					{
						try
						{
							unansweredTransactions.wait(10);
						}
						catch (InterruptedException e)
						{
							//TODO: Not sure if this is a good idea
							Thread.currentThread().interrupt();
						}
					}
				}
		
				return unansweredTransactions.size();
			}
			return -1;
		}
		finally
		{
			clientPool.close();
		}
	}


	@Override
	public String toString()
	{
		ToStringBuilder tsb = new ToStringBuilder(this);
		for(String server: upstreamServers)
		{
			tsb.append("server", server);
		}
		return tsb.build();
	}


	/**
	 * In milliseconds
	 * 
	 * @return
	 */
	public long getResponseTimeout()
	{
		return responseTimeout;
	}


	/**
	 * 
	 * @param responseTimeout in milliseconds
	 */
	public void setResponseTimeout(long responseTimeout)
	{
		this.responseTimeout = responseTimeout;
	}




	public boolean isCacheNegativeResponses()
	{
		return cacheNegativeResponses;
	}


	public void setCacheNegativeResponses(boolean cacheNegativeResponses)
	{
		this.cacheNegativeResponses = cacheNegativeResponses;
	}


	public int getMaxTotalRequestsPerSecond()
	{
		return maxTotalRequestsPerSecond;
	}


	public void setMaxTotalRequestsPerSecond(int maxTotalRequestsPerSecond)
	{
		this.maxTotalRequestsPerSecond = maxTotalRequestsPerSecond;
	}


	public int getMaxRequestsPerServerPerSecond()
	{
		return maxRequestsPerServerPerSecond;
	}


	public void setMaxRequestsPerServerPerSecond(int maxRequestsPerServerPerSecond)
	{
		this.maxRequestsPerServerPerSecond = maxRequestsPerServerPerSecond;
	}


	public boolean isRoundRobin()
	{
		return roundRobin;
	}


	public void setRoundRobin(boolean roundRobin)
	{
		this.roundRobin = roundRobin;
	}


	public int getRoundRobinDuplicateCount()
	{
		return roundRobinDuplicateCount;
	}


	public void setRoundRobinDuplicateCount(int roundRobinDuplicateCount)
	{
		if (roundRobinDuplicateCount > upstreamServers.size())
		{
			logger.debug("Round robin duplicate count can't be larger than the number of servers. Setting to the number of servers.");
			roundRobinDuplicateCount = upstreamServers.size();
		}
		this.roundRobinDuplicateCount = roundRobinDuplicateCount;
	}


	public DnsCache getCache()
	{
		return cache;
	}


	public void setCache(DnsCache cache)
	{
		this.cache = cache;
	}




}