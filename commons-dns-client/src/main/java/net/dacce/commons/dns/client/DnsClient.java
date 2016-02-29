package net.dacce.commons.dns.client;

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
import net.dacce.commons.dns.exceptions.DnsClientConnectException;
import net.dacce.commons.dns.messages.DnsMessage;
import net.dacce.commons.dns.messages.MessageType;
import net.dacce.commons.dns.messages.OpCode;
import net.dacce.commons.dns.messages.QuestionRecord;
import net.dacce.commons.dns.messages.ResponseCode;
import net.dacce.commons.dns.records.RecordType;
import net.dacce.commons.general.EventCounter;

public class DnsClient 
{
	private final static Logger logger = LoggerFactory.getLogger(DnsClient.class);
	private SecureRandom random;

	private Map<RequestKey, DnsTransaction> unansweredRequests;
	private int maxRequestsPerSecond = 30;
	private EventCounter requestCounter;
	private int connectTimeout = 5000;

	private int count;
	private boolean padSecondRequest;
	private DnsUdpConnection udpConnection;
	private final Map<DnsTcpConnection, RequestKey> tcpConnections;
	private final InetSocketAddress serverAddress;
	private boolean forceTcpZoneTransfer = true;
	
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

	
	public synchronized WriteFuture sendQuery(DnsTransaction transaction) throws DnsClientConnectException
	{
		return sendQuery(transaction, false);
	}
	
	/**
	 * 
	 * Query is asynchronous, but the connection is not. 
	 * @param question
	 * @param recurse
	 * @return DNS transaction ID
	 * @throws DnsClientConnectException 
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

	public void close(boolean immediately)
	{
		udpConnection.close(immediately);
		for (DnsTcpConnection t: tcpConnections.keySet())
		{
			t.close(immediately);
		}
	}
	
	
	private class RequestKey
	{
		QuestionRecord question;
		int transactionId;
		RequestKey(QuestionRecord question, int transactionId)
		{
			this.question = question;
			this.transactionId = transactionId;
		}
		@Override
		public int hashCode()
		{
			return new HashCodeBuilder().append(question).append(transactionId).toHashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			RequestKey r = (RequestKey) obj;
			return new EqualsBuilder().append(r.question, question).append(r.transactionId, transactionId).isEquals();
		}
	}
	

	
	
	/**
	 * For Google bug
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


	

	public int getTimeout()
	{
		return connectTimeout;
	}

	public void setTimeout(int timeout)
	{
		this.connectTimeout = timeout;
	}

	public int getConnectTimeout()
	{
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout)
	{
		this.connectTimeout = connectTimeout;
	}

	public int getMaxRequestsPerSecond()
	{
		return maxRequestsPerSecond;
	}

	public void setMaxRequestsPerSecond(int maxRequestsPerSecond)
	{
		this.maxRequestsPerSecond = maxRequestsPerSecond;
	}

	public boolean isPadSecondRequest()
	{
		return padSecondRequest;
	}

	public void setPadSecondRequest(boolean padSecondRequest)
	{
		this.padSecondRequest = padSecondRequest;
	}

	public boolean isForceTcpZoneTransfer()
	{
		return forceTcpZoneTransfer;
	}

	public void setForceTcpZoneTransfer(boolean forceTcpZoneTransfer)
	{
		this.forceTcpZoneTransfer = forceTcpZoneTransfer;
	}

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

	public InetSocketAddress getServerAddress()
	{
		return serverAddress;
	}

}
