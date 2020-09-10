package space.dcce.commons.dns.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import space.dcce.commons.dns.exceptions.DnsClientConnectException;

// TODO: Auto-generated Javadoc
/**
 * The Class DnsUdpConnection.
 */
public class DnsUdpConnection extends DnsConnection
{
	
	/**
	 * Instantiates a new dns udp connection.
	 *
	 * @param parent the parent
	 * @param serverAddress the server address
	 * @throws DnsClientConnectException the dns client connect exception
	 */
	public DnsUdpConnection(DnsClient parent, InetSocketAddress serverAddress) throws DnsClientConnectException
	{
		super(parent, serverAddress);
	}


	/**
	 * Make io connector.
	 *
	 * @return the io connector
	 */
	@Override
	protected IoConnector makeIoConnector()
	{
		return new NioDatagramConnector() ;
	}


	/**
	 * Checks if is tcp.
	 *
	 * @return true, if is tcp
	 */
	@Override
	public boolean isTcp()
	{
		return false;
	}
}
