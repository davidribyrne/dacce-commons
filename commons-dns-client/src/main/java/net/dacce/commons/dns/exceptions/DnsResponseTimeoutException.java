package net.dacce.commons.dns.exceptions;


public class DnsResponseTimeoutException extends DnsClientException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4906939264121357107L;


	public DnsResponseTimeoutException()
	{
	}


	public DnsResponseTimeoutException(String message)
	{
		super(message);
	}


	public DnsResponseTimeoutException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public DnsResponseTimeoutException(Throwable cause)
	{
		super(cause);
	}
	
}
