package space.dcce.commons.dns.client;

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

import space.dcce.commons.dns.exceptions.DnsClientConnectException;
import space.dcce.commons.dns.messages.DnsMessage;
import space.dcce.commons.dns.protocol.DnsProtocolTcpCodecFactory;
import space.dcce.commons.dns.protocol.DnsProtocolUdpCodecFactory;


// TODO: Auto-generated Javadoc
/**
 * The Class DnsConnection.
 */
public abstract class DnsConnection extends IoHandlerAdapter
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(DnsConnection.class);

	/** The connector. */
	private IoConnector connector;
	
	/** The connect timeout. */
	private int connectTimeout = 10000;
	
	/** The dns session. */
	private IoSession dnsSession;


	/**
	 * Make io connector.
	 *
	 * @return the io connector
	 */
	protected abstract IoConnector makeIoConnector();
	
	/**
	 * Checks if is tcp.
	 *
	 * @return true, if is tcp
	 */
	public abstract boolean isTcp();
	
	/** The server address. */
	final private InetSocketAddress serverAddress;
	
	/** The parent. */
	final private DnsClient parent;

	/**
	 * Instantiates a new dns connection.
	 *
	 * @param parent the parent
	 * @param serverAddress the server address
	 * @throws DnsClientConnectException the dns client connect exception
	 */
	public DnsConnection(DnsClient parent, InetSocketAddress serverAddress) throws DnsClientConnectException
	{
		this.parent = parent;
		this.serverAddress = serverAddress;
		connector = makeIoConnector();
		connector.setHandler(this);
		connect();
	}


	/**
	 * Session created.
	 *
	 * @param session the session
	 * @throws Exception the exception
	 */
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


	/**
	 * Message received.
	 *
	 * @param session the session
	 * @param message the message
	 * @throws Exception the exception
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{
		parent.messageReceived(this, (DnsMessage) message);
	}


	/**
	 * Connect.
	 *
	 * @throws DnsClientConnectException the dns client connect exception
	 */
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

	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public boolean isConnected()
	{
		return dnsSession != null && dnsSession.isConnected();
	}
	
	/**
	 * Close.
	 *
	 * @param immediately the immediately
	 * @return the close future
	 */
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

	
	
	/**
	 * Send message.
	 *
	 * @param message the message
	 * @return the write future
	 */
	public WriteFuture sendMessage(DnsMessage message)
	{
		return dnsSession.write(message);
	}

	
	/**
	 * Session closed.
	 *
	 * @param session the session
	 * @throws Exception the exception
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		logger.debug("DNS connection closed");
		super.sessionClosed(session);
	}
	
	/**
	 * Gets the read bytes.
	 *
	 * @return the read bytes
	 */
	public long getReadBytes()
	{
		return dnsSession.getReadBytes();
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("serverAddress", serverAddress).build();
	}

}
