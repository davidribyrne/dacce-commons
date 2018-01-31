package space.dcce.commons.dns.exceptions;


public class DnsClientConnectException extends DnsClientException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


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
