package net.dacce.commons.dns.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import net.dacce.commons.dns.exceptions.DnsClientConnectException;

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
