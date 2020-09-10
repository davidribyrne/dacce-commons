package space.dcce.commons.dns.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.dcce.commons.dns.exceptions.DnsClientConnectException;
import space.dcce.commons.general.UnexpectedException;

// TODO: Auto-generated Javadoc
/**
 * The Class DnsClientPool.
 */
public class DnsClientPool
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(DnsClientPool.class);
	
	
	/** The clients. */
	private final List<DnsClient> clients;
	
	/** The round robin. */
	private boolean roundRobin;
	
	/** The round robin duplicate count. */
	private int roundRobinDuplicateCount;
	
	/** The last server index. */
	private int lastServerIndex = -1;

	/**
	 * Instantiates a new dns client pool.
	 *
	 * @param serverAddresses the server addresses
	 * @param padSecondRequest the pad second request
	 * @param maxRequestsPerServerPerSecond the max requests per server per second
	 * @param roundRobin the round robin
	 * @param roundRobinDuplicateCount the round robin duplicate count
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public DnsClientPool(List<String> serverAddresses, boolean padSecondRequest, int maxRequestsPerServerPerSecond, 
			boolean roundRobin, int roundRobinDuplicateCount) throws IllegalArgumentException
	{
		if (serverAddresses == null || serverAddresses.isEmpty())
		{
			throw new IllegalArgumentException("No server addresses provided.");
		}
		if (roundRobinDuplicateCount > serverAddresses.size())
		{
			logger.debug("Round robin duplicate count can't be larger than the number of servers. Setting to the number of servers.");
			roundRobinDuplicateCount = serverAddresses.size();
		}
		if (roundRobinDuplicateCount == 0)
		{
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
	
	
	/**
	 * Choose next clients.
	 *
	 * @return the list
	 */
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
	
	/**
	 * Close.
	 */
	public void close()
	{
		for (DnsClient client: clients)
		{
			client.close(true);
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
		for (DnsClient client: clients)
		{
			tsb.append("client", client.getServerAddress().toString());
		}
		tsb.append("roundRobin", roundRobin);
		tsb.append("roundRobinDuplicateCount", roundRobinDuplicateCount);
		return tsb.build();
	}
	
}
