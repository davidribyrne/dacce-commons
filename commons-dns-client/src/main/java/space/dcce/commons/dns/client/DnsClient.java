package space.dcce.commons.dns.client;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.future.WriteFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.dcce.commons.dns.exceptions.DnsClientConnectException;
import space.dcce.commons.dns.messages.DnsMessage;
import space.dcce.commons.dns.messages.MessageType;
import space.dcce.commons.dns.messages.OpCode;
import space.dcce.commons.dns.messages.QuestionRecord;
import space.dcce.commons.dns.messages.ResponseCode;
import space.dcce.commons.dns.records.RecordType;
import space.dcce.commons.general.EventCounter;

// TODO: Auto-generated Javadoc
/**
 * The Class DnsClient.
 */
public class DnsClient 
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(DnsClient.class);
	
	/** The random. */
	private SecureRandom random;

	/** The unanswered requests. */
	private Map<RequestKey, DnsTransaction> unansweredRequests;
	
	/** The max requests per second. */
	private int maxRequestsPerSecond = 30;
	
	/** The request counter. */
	private EventCounter requestCounter;
	
	/** The connect timeout. */
	private int connectTimeout = 5000;

	/** The count. */
	private int count;
	
	/** The pad second request. */
	private boolean padSecondRequest;
	
	/** The udp connection. */
	private DnsUdpConnection udpConnection;
	
	/** The tcp connections. */
	private final Map<DnsTcpConnection, RequestKey> tcpConnections;
	
	/** The server address. */
	private final InetSocketAddress serverAddress;
	
	/** The force tcp zone transfer. */
	private boolean forceTcpZoneTransfer = true;
	
	/**
	 * Instantiates a new dns client.
	 *
	 * @param serverAddress the server address
	 * @param padSecondRequest the pad second request
	 * @throws DnsClientConnectException the dns client connect exception
	 */
	protected DnsClient(InetSocketAddress serverAddress, boolean padSecondRequest) throws DnsClientConnectException 
	{
		this.serverAddress = serverAddress;
		tcpConnections = new HashMap<DnsTcpConnection, RequestKey>(1);
		this.padSecondRequest = padSecondRequest;
		unansweredRequests = new HashMap<RequestKey, DnsTransaction>();
		random = new SecureRandom();
		requestCounter = new EventCounter(1000, true);
		udpConnection = new DnsUdpConnection(this, serverAddress);
	}
	
	/**
	 * Connection closed.
	 *
	 * @param connection the connection
	 */
	public void connectionClosed(DnsConnection connection)
	{
		// If the TCP connection closed without a response
		if (connection.isTcp() && connection.getReadBytes() == 0) 
		{
			logger.debug("TCP connection closed without reading data from server.");
			RequestKey requestKey = tcpConnections.remove(connection);
			synchronized(unansweredRequests)
			{
				DnsTransaction transaction = unansweredRequests.remove(requestKey);
				transaction.addNegativeResponse();
			}
		}
	}
	
	/**
	 * Message received.
	 *
	 * @param connection the connection
	 * @param response the response
	 * @throws Exception the exception
	 */
	public void messageReceived(DnsConnection connection, DnsMessage response) throws Exception
	{
		logger.trace("DnsClient has received response message.");
		if (connection.isTcp())
		{
			logger.trace("The message was on a TCP connection so we're closing it");
			connection.close(true);
			tcpConnections.remove(connection);
		}
		
		RequestKey key = new RequestKey(response.getQuestionRecords().get(0), response.getTransactionId());
		DnsTransaction transaction = null;
		synchronized (unansweredRequests)
		{
			logger.trace("{} unanswered requests remain in DNS client. Starting to prune.", unansweredRequests.size());
			if (unansweredRequests.containsKey(key))
			{
				transaction = unansweredRequests.remove(key);
				if (!response.isTruncated())
				{
					transaction.addAnswers(response.getAnswerRecords());
					return;
				}
				if(connection.isTcp())
				{
					logger.error("A TCP DNS response was truncated? That makes no sense.");
					return;
				}
			}
			else
			{
				logger.debug("DNS response recieved, but there is no record of request.");
				return;
			}
		}
		
		// If we get here, it's because the request needs to be repeated over TCP
		sendQuery(transaction, true);
	}

	
	/**
	 * Send query.
	 *
	 * @param transaction the transaction
	 * @return the write future
	 * @throws DnsClientConnectException the dns client connect exception
	 */
	public synchronized WriteFuture sendQuery(DnsTransaction transaction) throws DnsClientConnectException
	{
		return sendQuery(transaction, false);
	}
	
	/**
	 * Query is asynchronous, but the connection is not. 
	 *
	 * @param transaction the transaction
	 * @param tcp the tcp
	 * @return DNS transaction ID
	 * @throws DnsClientConnectException the dns client connect exception
	 */
	public synchronized WriteFuture sendQuery(DnsTransaction transaction, boolean tcp) throws DnsClientConnectException
	{
		DnsConnection connection;
		logger.trace("Starting sendQuery");

		count++;
		DnsMessage message = new DnsMessage ();
		message.setTransactionId(random.nextInt() & 0xffff);
		message.setQuestionRecords(Collections.singletonList(transaction.getQuestion()));
		message.setMessageType(MessageType.QUERY);
		message.setOpCode(OpCode.QUERY);
		message.setRecursionDesired(transaction.isRecurse());
		message.setResponseCode(ResponseCode.NO_ERROR);
		
		RequestKey key = new RequestKey(transaction.getQuestion(), message.getTransactionId());

		if (transaction.getQuestion().getRecordType().equals(RecordType.AXFR) && forceTcpZoneTransfer)
			tcp = true;
		
		if (tcp)
		{
			DnsTcpConnection tcpConnection = new DnsTcpConnection(this, serverAddress);
			tcpConnections.put(tcpConnection, key);
			connection = tcpConnection;
		}
		else
			connection = udpConnection;
		
		if (count == 2 && isPadSecondRequest() && !tcp)
		{
			WriteFuture wf = sendDummyQuery();
			wf.getException();
		}
		synchronized(unansweredRequests)
		{
			if (unansweredRequests.containsKey(key))
			{
				logger.error("Duplicate DNS transaction ID (" + message.getTransactionId() + ") in queries for " + transaction.getQuestion().getDomainName() + ".");
			}
			else
			{
				unansweredRequests.put(key, transaction);
			}
		}
		try
		{
			requestCounter.waitForThrottle(maxRequestsPerSecond);
		}
		catch (InterruptedException e)
		{
			close(true);
			Thread.currentThread().interrupt();
		}
		requestCounter.trackEvent();
		return connection.sendMessage(message);
	}

	/**
	 * Close.
	 *
	 * @param immediately the immediately
	 */
	public void close(boolean immediately)
	{
		udpConnection.close(immediately);
		for (DnsTcpConnection t: tcpConnections.keySet())
		{
			t.close(immediately);
		}
	}
	
	
	/**
	 * The Class RequestKey.
	 */
	private class RequestKey
	{
		
		/** The question. */
		QuestionRecord question;
		
		/** The transaction id. */
		int transactionId;
		
		/**
		 * Instantiates a new request key.
		 *
		 * @param question the question
		 * @param transactionId the transaction id
		 */
		RequestKey(QuestionRecord question, int transactionId)
		{
			this.question = question;
			this.transactionId = transactionId;
		}
		
		/**
		 * Hash code.
		 *
		 * @return the int
		 */
		@Override
		public int hashCode()
		{
			return new HashCodeBuilder().append(question).append(transactionId).toHashCode();
		}
		
		/**
		 * Equals.
		 *
		 * @param obj the obj
		 * @return true, if successful
		 */
		@Override
		public boolean equals(Object obj)
		{
			RequestKey r = (RequestKey) obj;
			return new EqualsBuilder().append(r.question, question).append(r.transactionId, transactionId).isEquals();
		}
	}
	

	
	
	/**
	 * For Google bug.
	 *
	 * @return the write future
	 */
	private WriteFuture sendDummyQuery()
	{
		DnsMessage message = new DnsMessage ();
		message.setTransactionId(0);
		message.setQuestionRecords(Collections.singletonList(new QuestionRecord("www.google.com", RecordType.A)));
		message.setMessageType(MessageType.QUERY);
		message.setOpCode(OpCode.QUERY);
		message.setRecursionDesired(true);
		message.setResponseCode(ResponseCode.NO_ERROR);
		return udpConnection.sendMessage(message);
	}


	

	/**
	 * Gets the timeout.
	 *
	 * @return the timeout
	 */
	public int getTimeout()
	{
		return connectTimeout;
	}

	/**
	 * Sets the timeout.
	 *
	 * @param timeout the new timeout
	 */
	public void setTimeout(int timeout)
	{
		this.connectTimeout = timeout;
	}

	/**
	 * Gets the connect timeout.
	 *
	 * @return the connect timeout
	 */
	public int getConnectTimeout()
	{
		return connectTimeout;
	}

	/**
	 * Sets the connect timeout.
	 *
	 * @param connectTimeout the new connect timeout
	 */
	public void setConnectTimeout(int connectTimeout)
	{
		this.connectTimeout = connectTimeout;
	}

	/**
	 * Gets the max requests per second.
	 *
	 * @return the max requests per second
	 */
	public int getMaxRequestsPerSecond()
	{
		return maxRequestsPerSecond;
	}

	/**
	 * Sets the max requests per second.
	 *
	 * @param maxRequestsPerSecond the new max requests per second
	 */
	public void setMaxRequestsPerSecond(int maxRequestsPerSecond)
	{
		this.maxRequestsPerSecond = maxRequestsPerSecond;
	}

	/**
	 * Checks if is pad second request.
	 *
	 * @return true, if is pad second request
	 */
	public boolean isPadSecondRequest()
	{
		return padSecondRequest;
	}

	/**
	 * Sets the pad second request.
	 *
	 * @param padSecondRequest the new pad second request
	 */
	public void setPadSecondRequest(boolean padSecondRequest)
	{
		this.padSecondRequest = padSecondRequest;
	}

	/**
	 * Checks if is force tcp zone transfer.
	 *
	 * @return true, if is force tcp zone transfer
	 */
	public boolean isForceTcpZoneTransfer()
	{
		return forceTcpZoneTransfer;
	}

	/**
	 * Sets the force tcp zone transfer.
	 *
	 * @param forceTcpZoneTransfer the new force tcp zone transfer
	 */
	public void setForceTcpZoneTransfer(boolean forceTcpZoneTransfer)
	{
		this.forceTcpZoneTransfer = forceTcpZoneTransfer;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.append("unansweredRequests", unansweredRequests)
				.append("maxRequestsPerSecond", maxRequestsPerSecond)
				.append("requestCounter", requestCounter)
				.append("connectTimeout", connectTimeout)
				.append("count", count)
				.append("padSecondRequest", padSecondRequest)
				.append("udpConnection", udpConnection)
				.append("tcpConnections", tcpConnections)
				.append("serverAddress", serverAddress)
				.append("forceTcpZoneTransfer", forceTcpZoneTransfer)
				.build();
	}

	/**
	 * Gets the server address.
	 *
	 * @return the server address
	 */
	public InetSocketAddress getServerAddress()
	{
		return serverAddress;
	}

}
