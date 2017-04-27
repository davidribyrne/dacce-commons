package net.dacce.commons.dns.client;

import java.net.InetSocketAddress;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dacce.commons.dns.exceptions.DnsClientConnectException;
import net.dacce.commons.dns.messages.DnsMessage;
import net.dacce.commons.dns.protocol.DnsProtocolTcpCodecFactory;
import net.dacce.commons.dns.protocol.DnsProtocolUdpCodecFactory;


public abstract class DnsConnection extends IoHandlerAdapter
{
	private final static Logger logger = LoggerFactory.getLogger(DnsConnection.class);

	private IoConnector connector;
	private int connectTimeout = 10000;
	private IoSession dnsSession;


	protected abstract IoConnector makeIoConnector();
	public abstract boolean isTcp();
	
	final private InetSocketAddress serverAddress;
	final private DnsClient parent;

	public DnsConnection(DnsClient parent, InetSocketAddress serverAddress) throws DnsClientConnectException
	{
		this.parent = parent;
		this.serverAddress = serverAddress;
		connector = makeIoConnector();
		connector.setHandler(this);
		connect();
	}


	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		if (session.getTransportMetadata().isConnectionless())
		{
			session.getFilterChain().addFirst("codec",
					new ProtocolCodecFilter(DnsProtocolUdpCodecFactory.getInstance()));
		}
		else
		{
			session.getFilterChain().addFirst("codec",
					new ProtocolCodecFilter(DnsProtocolTcpCodecFactory.getInstance()));
		}
	}


	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{
		parent.messageReceived(this, (DnsMessage) message);
	}


	public void connect() throws DnsClientConnectException
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
			// TODO: Not sure if this is a good idea
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
			logger.debug("Can't close {} session. DNS session not created.", isTcp() ? "TCP" : "UDP");
			return null;
		}
		if (!dnsSession.isConnected())
		{
			logger.debug("Can't close {} session. DNS session not connected.", isTcp() ? "TCP" : "UDP");
			return null;
		}
		IoService s = dnsSession.getService();
		s.dispose(false);
		return dnsSession.close(immediately);
	}

	
	
	public WriteFuture sendMessage(DnsMessage message)
	{
		return dnsSession.write(message);
	}

	
	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		logger.debug("DNS connection closed");
		super.sessionClosed(session);
	}
	
	public long getReadBytes()
	{
		return dnsSession.getReadBytes();
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("serverAddress", serverAddress).build();
	}

}
