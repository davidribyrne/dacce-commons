package net.dacce.commons.dns.exceptions;


public class DnsClientException extends Exception
{

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
