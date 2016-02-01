package net.dacce.commons.dns.client;

import java.net.InetSocketAddress;
import net.dacce.commons.dns.exceptions.DnsClientConnectException;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

public class DnsUdpConnection extends DnsConnection
{
	public DnsUdpConnection(DnsClient parent, InetSocketAddress serverAddress) throws DnsClientConnectException
	{
		super(parent, serverAddress);
	}


	@Override
	protected IoConnector makeIoConnector()
	{
		return new NioDatagramConnector() ;
	}


	@Override
	public boolean isTcp()
	{
		return false;
	}
}
