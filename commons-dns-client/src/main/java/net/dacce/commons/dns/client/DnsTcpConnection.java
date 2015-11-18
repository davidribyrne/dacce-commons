package net.dacce.commons.dns.client;

import net.dacce.commons.dns.exceptions.DnsClientConnectException;
import net.dacce.commons.dns.messages.DnsMessage;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;

public class DnsTcpConnection extends DnsConnection
{
	private final static Logger logger = LoggerFactory.getLogger(DnsTcpConnection.class);

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
