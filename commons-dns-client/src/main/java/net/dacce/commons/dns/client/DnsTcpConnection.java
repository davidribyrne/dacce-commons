package net.dacce.commons.dns.client;

import java.net.InetSocketAddress;
import net.dacce.commons.dns.exceptions.DnsClientConnectException;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class DnsTcpConnection extends DnsConnection
{
	public DnsTcpConnection(DnsClient parent, InetSocketAddress serverAddress) throws DnsClientConnectException
	{
		super(parent, serverAddress);
	}


	@Override
	protected IoConnector makeIoConnector()
	{
		return new NioSocketConnector();
	}


	@Override
	public boolean isTcp()
	{
		return true;
	}


}
