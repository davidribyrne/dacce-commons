package net.dacce.commons.dns.client;

import net.dacce.commons.dns.client.cache.SimpleDnsCache;
import net.dacce.commons.dns.client.exceptions.DnsClientConnectException;
import net.dacce.commons.dns.client.exceptions.DnsResponseTimeoutException;
import net.dacce.commons.dns.client.exceptions.NoRecordFound;
import net.dacce.commons.general.StringUtils;
import net.dacce.commons.netaddr.IPUtils;
import net.dacce.commons.netaddr.InvalidIPAddressFormatException;
import net.dacce.commons.netaddr.SimpleInetAddress;

import org.apache.directory.server.dns.messages.DnsMessage;
import org.apache.directory.server.dns.messages.DnsMessageModifier;
import org.apache.directory.server.dns.messages.QuestionRecord;
import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;
import org.apache.directory.server.dns.records.ARecord;
import org.apache.directory.server.dns.records.ResourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;


public class Resolver
{
	private final static Logger logger = LoggerFactory.getLogger(Resolver.class);
	private final List<InetSocketAddress> upstreamServers;
	private boolean roundRobin;
	private final SimpleDnsCache cache = new SimpleDnsCache();
	private long timeout = 30000;
	private boolean cacheNegativeResponses = true;
	private int nextServerIndex = 0;
	
	/** Wait for responses from all DNS servers before declaring a negative response */
	private boolean waitForAllResponses = true;

	
	
	/**
	 * Attempt to use system DNS servers. Note that Java does not have a reliable way to determine this.
	 */
	public Resolver()
	{
		this(SystemDnsServers.getSystemDnsServers());
	}


	public Resolver(List<String> upstreamServers)
	{
		this.upstreamServers = getSocketsFromStrings(upstreamServers);
	}

	private synchronized int getNextServerIndex()
	{
		if (nextServerIndex == upstreamServers.size())
			nextServerIndex = 0;
		return nextServerIndex++;
	}

	private List<InetSocketAddress> getSocketsFromStrings(List<String> servers)
	{
		List<InetSocketAddress> sockets = new ArrayList<InetSocketAddress>(1);
		for (String server : servers)
		{
			sockets.add(new InetSocketAddress(server, 53));
		}
		return sockets;
	}


	public List<SimpleInetAddress> simpleResolve(String hostname, boolean useCache) throws DnsResponseTimeoutException, DnsClientConnectException, NoRecordFound
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


	private DnsClient[] getClients()
	{
		DnsClient[] clients;
		if (roundRobin)
		{
			clients = new DnsClient[0];
			clients[0] = new DnsUdpClient(upstreamServers.get(getNextServerIndex()));
		}
		else
		{
			clients = new DnsClient[upstreamServers.size()];
			for (int i = 0; i < clients.length; i++)
			{
				clients[i] = new DnsUdpClient(upstreamServers.get(i));
			}
		}
		return clients;
	}

	private void closeClients(DnsClient[] clients)
	{
		for(DnsClient client: clients)
		{
			if (client.isConnected())
				client.close(true);
		}
	}
	
	public List<ResourceRecord> query(QuestionRecord question, boolean useCache, boolean recurse) throws DnsClientConnectException, DnsResponseTimeoutException, NoRecordFound
	{
		if (useCache && cache.contains(question))
			return cache.get(question);

		DnsClient[] clients = getClients();
		int[] messageIds = new int[clients.length];
		
		Exception[] exceptions = new Exception[clients.length];
		int exceptionCount = 0;
		for (int i = 0; i < clients.length; i++)
		{
			try
			{
				messageIds[i] = clients[i].asyncQuery(question, recurse).getTransactionId();
			}
			catch (DnsClientConnectException e)
			{
				exceptionCount++;
				exceptions[i] = e;
				logger.debug("Problem with DNS client: " + e.getLocalizedMessage(), e);
			}
		}
		
		// All clients failed to connect
		if (exceptionCount == clients.length)
		{
			throw new DnsClientConnectException("Failed to connect to DNS servers");
		}

		try
		{
			long start = System.currentTimeMillis();
			int responseCount = 0;
			do
			{
				responseCount = 0;
				for (int i = 0; i < messageIds.length; i++)
				{
					DnsMessage response = clients[i].getResponse(messageIds[i]);
					if (response != null)
					{
						if (!waitForAllResponses || !response.getAnswerRecords().isEmpty())
						{
							cache.add(question, response.getAnswerRecords());
							return response.getAnswerRecords();
						}
						responseCount++;
					}
				}
				
				// We've seen all of the responses and they are all empty
				if (responseCount == clients.length)
				{
					break;
				}
				synchronized(Thread.currentThread())
				{
					try
					{
						Thread.currentThread().wait(10);
					}
					catch (InterruptedException e)
					{
						logger.trace("DNS client wait interrupted: " + e.getLocalizedMessage(), e);
						Thread.currentThread().interrupt();
					}
				}
			} while (start + timeout > System.currentTimeMillis());
			
			// If we saw at least one empty response
			if (responseCount > 0)
			{
				if (cacheNegativeResponses)
				{
					cache.add(question, Collections.emptyList());
				}
				throw new NoRecordFound();
			}
			throw new DnsResponseTimeoutException("Query timed out after " + timeout + " miliseconds.");
		}
		finally
		{
			closeClients(clients);
		}
	}


	public void bulkQuery(List<DnsTransaction> questions, boolean useCache, boolean recurse)
	{

	}


	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}


	@Override
	public int hashCode()
	{
		return super.hashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}


	/**
	 * In miliseconds
	 * 
	 * @return
	 */
	public long getTimeout()
	{
		return timeout;
	}


	/**
	 * 
	 * @param timeout
	 *            in miliseconds
	 */
	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}


	public boolean isRoundRobin()
	{
		return roundRobin;
	}


	public void setRoundRobin(boolean roundRobin)
	{
		this.roundRobin = roundRobin;
	}


	public boolean isCacheNegativeResponses()
	{
		return cacheNegativeResponses;
	}


	public void setCacheNegativeResponses(boolean cacheNegativeResponses)
	{
		this.cacheNegativeResponses = cacheNegativeResponses;
	}


}
