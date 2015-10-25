package net.dacce.commons.dns;

import org.apache.directory.server.dns.messages.DnsMessage;
import org.apache.directory.server.dns.messages.DnsMessageModifier;
import org.apache.directory.server.dns.messages.MessageType;
import org.apache.directory.server.dns.messages.OpCode;
import org.apache.directory.server.dns.messages.QuestionRecord;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.*;

public abstract class DnsClient
{
	private final static Logger logger = LoggerFactory.getLogger(DnsClient.class);
	private SecureRandom random;

	private IoConnector connector;
	private ConnectFuture connFuture;
	protected abstract IoConnector makeIoConnector();
	private IoSession session;
	private Map<Integer, DnsMessage> pendingRequests;
	
	public DnsClient(InetSocketAddress serverAddress)
	{
		pendingRequests = new HashMap<Integer, DnsMessage>();
		random = new SecureRandom();
		connector = makeIoConnector();
		connector.setHandler(new DnsClientHandler());
		connFuture = connector.connect(serverAddress);
		connFuture.addListener(new IoFutureListener<ConnectFuture>()
		{
			@Override
			public void operationComplete(ConnectFuture future)
			{
				if(future.isConnected())
				{
					session = future.getSession();
				}
			}
		});
	}

	/**
	 * 
	 * @param question
	 * @param recursion
	 * @return DNS transaction ID
	 */
	public int asyncQuery(QuestionRecord question, boolean recursion)
	{
		DnsMessageModifier modifier = new DnsMessageModifier ();
		modifier.setTransactionId(random.nextInt());
		modifier.setQuestionRecords(Collections.singletonList(question));
		modifier.setMessageType(MessageType.QUERY);
		modifier.setOpCode(OpCode.QUERY);
		modifier.setRecursionDesired(recursion);
		
		DnsMessage message = modifier.getDnsMessage();
		asyncQuery(message);
		return message.getTransactionId();
	}

	public void asyncQuery(DnsMessage message)
	{
		if (session == null)
		{
			throw new IllegalStateException("Session not created.");
		}

		synchronized(pendingRequests)
		{
			if (pendingRequests.containsKey(message.getTransactionId()))
			{
				throw new IllegalArgumentException("Duplicate DNS transaction ID (" + message.getTransactionId() + ").");
			}
			pendingRequests.put(message.getTransactionId(), message);
		}
		session.write(message);
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

}
