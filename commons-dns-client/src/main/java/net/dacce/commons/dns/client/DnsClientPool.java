package net.dacce.commons.dns.client;

import net.dacce.commons.dns.exceptions.DnsClientConnectException;
import net.dacce.commons.general.UnexpectedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;

public class DnsClientPool
{
	private final static Logger logger = LoggerFactory.getLogger(DnsClientPool.class);
	
	
	private final List<DnsClient> clients;
	private boolean roundRobin;
	private int roundRobinDuplicateCount;
	private int lastServerIndex = -1;

	public DnsClientPool(List<String> serverAddresses, boolean padSecondRequest, int maxRequestsPerServerPerSecond, 
			boolean roundRobin, int roundRobinDuplicateCount)
	{
		if (roundRobinDuplicateCount > serverAddresses.size())
		{
			logger.debug("Round robin duplicate count can't be larger than the number of servers. Setting to the number of servers.");
			roundRobinDuplicateCount = serverAddresses.size();
		}
		this.roundRobin = roundRobin;
		this.roundRobinDuplicateCount = roundRobinDuplicateCount;
		clients = new ArrayList<DnsClient>(serverAddresses.size());
		for (String address : serverAddresses)
		{
			try
			{
				InetSocketAddress sa = new InetSocketAddress(address, 53);
				DnsClient client = new DnsClient(sa, padSecondRequest);
				client.setMaxRequestsPerSecond(maxRequestsPerServerPerSecond);
				client.setPadSecondRequest(padSecondRequest);
				clients.add(client);
			}
			catch (DnsClientConnectException e)
			{
				throw new UnexpectedException("Why did a UDP DNS client throw a connect exception? " + e.getLocalizedMessage(), e);
			}
		}
	}
	
	
	public synchronized List<DnsClient> chooseNextClients()
	{
		if (!roundRobin)
			return clients;

		List<DnsClient> nextClients = new ArrayList<DnsClient>(roundRobinDuplicateCount);
		for (int i = 0; i < roundRobinDuplicateCount; i++)
		{
			if (++lastServerIndex >= clients.size())
				lastServerIndex = 0;
			nextClients.add(clients.get(lastServerIndex));
		}
		
		return nextClients;
	}
	
	public void close()
	{
		for (DnsClient client: clients)
		{
			client.close(true);
		}
	}
	
}