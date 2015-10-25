package net.dacce.commons.dns;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;

public class DnsUdpClient extends DnsClient
{
	private final static Logger logger = LoggerFactory.getLogger(DnsUdpClient.class);

	private IoConnector connector;
	
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


	public DnsUdpClient(InetSocketAddress serverAddress)
	{
		super(serverAddress);

		
	}


	@Override
	protected IoConnector makeIoConnector()
	{
		return new NioDatagramConnector() ;
	}
}
