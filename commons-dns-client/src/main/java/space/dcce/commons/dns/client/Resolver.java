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


// TODO: Auto-generated Javadoc
/**
 * The Class Resolver.
 */
public class Resolver
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(Resolver.class);

/** The total request counter. */
//	private final DnsClientPool clientPool;
	private final EventCounter totalRequestCounter;

	/** The cache. */
	private DnsCache cache = new SimpleDnsCache();
	
	/** The response timeout. */
	private long responseTimeout = 10000;
	
	/** The cache negative responses. */
	private boolean cacheNegativeResponses = true;
	
	/** The max total requests per second. */
	private int maxTotalRequestsPerSecond = 100;
	
	/** The max requests per server per second. */
	private int maxRequestsPerServerPerSecond = 30;
	
	/** The Constant PUBLIC_DNS_SERVERS_FILE. */
	private static final String PUBLIC_DNS_SERVERS_FILE = "/public_dns_servers.txt";
	
	/** The upstream servers. */
	private final List<String> upstreamServers;
	
	/** The pad second request. */
	private boolean padSecondRequest;
	
	/** The round robin. */
	private boolean roundRobin = true;
	
	/** The round robin duplicate count. */
	private int roundRobinDuplicateCount;
	
	/**
	 * Attempt to use system DNS servers. Note that Java does not have a reliable way to determine this.
	 */
	public Resolver()
	{
		this(SystemDnsServers.getSystemDnsServers(), true);
	}


	/**
	 * Instantiates a new resolver.
	 *
	 * @param upstreamServers the upstream servers
	 * @param padSecondRequest Makes the second request a throw-away to accommodate Google, et al
	 * @throws IllegalArgumentException the illegal argument exception
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
	

	/**
	 * Creates the public servers resolver.
	 *
	 * @return the resolver
	 */
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


	/**
	 * Simple resolve.
	 *
	 * @param hostname the hostname
	 * @param useCache the use cache
	 * @return the list
	 * @throws DnsResponseTimeoutException the dns response timeout exception
	 * @throws DnsClientConnectException the dns client connect exception
	 * @throws DnsNoRecordFoundException the dns no record found exception
	 */
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


	/**
	 * Query.
	 *
	 * @param question the question
	 * @param useCache the use cache
	 * @param recurse the recurse
	 * @return the list
	 * @throws DnsClientConnectException the dns client connect exception
	 * @throws DnsResponseTimeoutException the dns response timeout exception
	 * @throws DnsNoRecordFoundException the dns no record found exception
	 */
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
	 * If blocking, returns number of unanswered queries.
	 *
	 * @param transactions the transactions
	 * @param useCache the use cache
	 * @param block the block
	 * @return the int
	 * @throws DnsClientConnectException the dns client connect exception
	 * @throws DnsResponseTimeoutException the dns response timeout exception
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


	/**
	 * To string.
	 *
	 * @return the string
	 */
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
	 * In milliseconds.
	 *
	 * @return the response timeout
	 */
	public long getResponseTimeout()
	{
		return responseTimeout;
	}


	/**
	 * Sets the response timeout.
	 *
	 * @param responseTimeout in milliseconds
	 */
	public void setResponseTimeout(long responseTimeout)
	{
		this.responseTimeout = responseTimeout;
	}




	/**
	 * Checks if is cache negative responses.
	 *
	 * @return true, if is cache negative responses
	 */
	public boolean isCacheNegativeResponses()
	{
		return cacheNegativeResponses;
	}


	/**
	 * Sets the cache negative responses.
	 *
	 * @param cacheNegativeResponses the new cache negative responses
	 */
	public void setCacheNegativeResponses(boolean cacheNegativeResponses)
	{
		this.cacheNegativeResponses = cacheNegativeResponses;
	}


	/**
	 * Gets the max total requests per second.
	 *
	 * @return the max total requests per second
	 */
	public int getMaxTotalRequestsPerSecond()
	{
		return maxTotalRequestsPerSecond;
	}


	/**
	 * Sets the max total requests per second.
	 *
	 * @param maxTotalRequestsPerSecond the new max total requests per second
	 */
	public void setMaxTotalRequestsPerSecond(int maxTotalRequestsPerSecond)
	{
		this.maxTotalRequestsPerSecond = maxTotalRequestsPerSecond;
	}


	/**
	 * Gets the max requests per server per second.
	 *
	 * @return the max requests per server per second
	 */
	public int getMaxRequestsPerServerPerSecond()
	{
		return maxRequestsPerServerPerSecond;
	}


	/**
	 * Sets the max requests per server per second.
	 *
	 * @param maxRequestsPerServerPerSecond the new max requests per server per second
	 */
	public void setMaxRequestsPerServerPerSecond(int maxRequestsPerServerPerSecond)
	{
		this.maxRequestsPerServerPerSecond = maxRequestsPerServerPerSecond;
	}


	/**
	 * Checks if is round robin.
	 *
	 * @return true, if is round robin
	 */
	public boolean isRoundRobin()
	{
		return roundRobin;
	}


	/**
	 * Sets the round robin.
	 *
	 * @param roundRobin the new round robin
	 */
	public void setRoundRobin(boolean roundRobin)
	{
		this.roundRobin = roundRobin;
	}


	/**
	 * Gets the round robin duplicate count.
	 *
	 * @return the round robin duplicate count
	 */
	public int getRoundRobinDuplicateCount()
	{
		return roundRobinDuplicateCount;
	}


	/**
	 * Sets the round robin duplicate count.
	 *
	 * @param roundRobinDuplicateCount the new round robin duplicate count
	 */
	public void setRoundRobinDuplicateCount(int roundRobinDuplicateCount)
	{
		if (roundRobinDuplicateCount > upstreamServers.size())
		{
			logger.debug("Round robin duplicate count can't be larger than the number of servers. Setting to the number of servers.");
			roundRobinDuplicateCount = upstreamServers.size();
		}
		this.roundRobinDuplicateCount = roundRobinDuplicateCount;
	}


	/**
	 * Gets the cache.
	 *
	 * @return the cache
	 */
	public DnsCache getCache()
	{
		return cache;
	}


	/**
	 * Sets the cache.
	 *
	 * @param cache the new cache
	 */
	public void setCache(DnsCache cache)
	{
		this.cache = cache;
	}




}
