package net.dacce.commons.dns.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DnsClientConnectException extends DnsClientException
{
	private final static Logger logger = LoggerFactory.getLogger(DnsClientConnectException.class);



	public DnsClientConnectException()
	{
	}


	public DnsClientConnectException(String message)
	{
		super(message);
	}


	public DnsClientConnectException(Throwable cause)
	{
		super(cause);
	}


	public DnsClientConnectException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public DnsClientConnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
