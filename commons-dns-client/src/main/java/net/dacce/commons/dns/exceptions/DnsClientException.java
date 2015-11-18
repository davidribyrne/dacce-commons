package net.dacce.commons.dns.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DnsClientException extends Exception
{
	private final static Logger logger = LoggerFactory.getLogger(DnsClientException.class);




	public DnsClientException()
	{
	}


	public DnsClientException(String message)
	{
		super(message);
	}


	public DnsClientException(Throwable cause)
	{
		super(cause);
	}


	public DnsClientException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public DnsClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
