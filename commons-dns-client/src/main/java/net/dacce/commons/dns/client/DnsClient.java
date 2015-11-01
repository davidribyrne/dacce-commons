package net.dacce.commons.dns.client;

import net.dacce.commons.dns.client.exceptions.DnsClientConnectException;
import net.dacce.commons.dns.client.exceptions.DnsResponseTimeoutException;

import org.apache.directory.server.dns.messages.DnsMessage;
import org.apache.directory.server.dns.messages.DnsMessageModifier;
import org.apache.directory.server.dns.messages.MessageType;
import org.apache.directory.server.dns.messages.OpCode;
import org.apache.directory.server.dns.messages.QuestionRecord;
import org.apache.directory.server.dns.messages.ResponseCode;
import org.apache.directory.server.dns.protocol.DnsProtocolTcpCodecFactory;
import org.apache.directory.server.dns.protocol.DnsProtocolUdpCodecFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.IllegalSelectorException;
import java.security.SecureRandom;
import java.util.*;

public abstract class DnsClient extends IoHandlerAdapter
{
	private final static Logger logger = LoggerFactory.getLogger(DnsClient.class);
	private SecureRandom random;

	private IoConnector connector;
	protected abstract IoConnector makeIoConnector();
	private IoSession dnsSession;
	private Map<Integer, DnsMessage> pendingRequests;
	private Map<Integer, DnsMessage> responses;
	private int connectTimeout = 30000;
	final private InetSocketAddress serverAddress;
	
	public DnsClient(InetSocketAddress serverAddress) 
	{
		this.serverAddress = serverAddress;
		pendingRequests = new HashMap<Integer, DnsMessage>();
		responses = new HashMap<Integer, DnsMessage>();
		random = new SecureRandom();
		connector = makeIoConnector();
		connector.setHandler(this);
	}
	
	protected void connect() throws DnsClientConnectException
	{
		ConnectFuture connFuture = connector.connect(serverAddress);
		try
		{
			if (!connFuture.await(connectTimeout))
			{
				throw new DnsClientConnectException("Failed to connect to server (" + serverAddress.toString() + ").");
			}
			dnsSession = connFuture.getSession();
		}
		catch (InterruptedException e)
		{
			logger.trace("DNS client connect interrupted: " + e.getLocalizedMessage(), e);
			Thread.currentThread().interrupt();
		}
		if (!connFuture.isConnected())
		{
			throw new DnsClientConnectException("Timeout after " + connectTimeout + " ms connecting to " + serverAddress.toString());
		}
	}
	
	
	public boolean isConnected()
	{
		return dnsSession != null && dnsSession.isConnected();
	}
	
	public CloseFuture close(boolean immediately)
	{
		if (dnsSession == null)
		{
			logger.trace("Can't close. DNS session not created.");
		}
		if (dnsSession.isConnected())
		{
			logger.trace("Can't close. DNS session not connected.");
		}
		IoService s = dnsSession.getService();
		s.dispose(true);
		return dnsSession.close(immediately);
	}

	@Override
    public void sessionCreated( IoSession session ) throws Exception
    {
        if ( session.getTransportMetadata().isConnectionless() )
        {
            session.getFilterChain().addFirst( "codec",
                new ProtocolCodecFilter( DnsProtocolUdpCodecFactory.getInstance() ) );
        }
        else
        {
            session.getFilterChain().addFirst( "codec",
                new ProtocolCodecFilter( DnsProtocolTcpCodecFactory.getInstance() ) );
        }
    }

	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{
		logger.debug( "{} RCVD:  {}", session.getRemoteAddress(), message );
		DnsMessage response = (DnsMessage) message;
		responses.put(response.getTransactionId(), response);
	}
	
	
	public DnsMessage getResponse(int transactionId)
	{
		return responses.get(transactionId);
	}
	
	/**
	 * 
	 * Query is asynchronous, but the connection is not
	 * 
	 * @param question
	 * @param recursion
	 * @return DNS transaction ID
	 * @throws InterruptedException 
	 * @throws DnsResponseTimeoutException 
	 * @throws DnsClientConnectException 
	 */
	public DnsMessage asyncQuery(QuestionRecord question, boolean recursion) throws DnsResponseTimeoutException, DnsClientConnectException
	{
		DnsMessageModifier modifier = new DnsMessageModifier ();
		modifier.setTransactionId(random.nextInt() & 0xffff);
		modifier.setQuestionRecords(Collections.singletonList(question));
		modifier.setMessageType(MessageType.QUERY);
		modifier.setOpCode(OpCode.QUERY);
		modifier.setRecursionDesired(recursion);
		modifier.setResponseCode(ResponseCode.NO_ERROR);
		DnsMessage message = modifier.getDnsMessage();
		asyncQuery(message);
		return message;
	}

	/**
	 * Query is asynchronous, but the connection is not
	 * @param message
	 * @return
	 * @throws DnsResponseTimeoutException
	 * @throws DnsClientConnectException 
	 * @throws InterruptedException
	 */
	public WriteFuture asyncQuery(DnsMessage message) throws DnsClientConnectException
	{
		connect();
		
		synchronized(pendingRequests)
		{
			if (pendingRequests.containsKey(message.getTransactionId()))
			{
				throw new IllegalArgumentException("Duplicate DNS transaction ID (" + message.getTransactionId() + ").");
			}
			pendingRequests.put(message.getTransactionId(), message);
		}
		return dnsSession.write(message);
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

}
