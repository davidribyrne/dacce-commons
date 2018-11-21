package space.dcce.commons.dns.exceptions;


public class DnsClientException extends Exception
{

	private static final long serialVersionUID = 1L;


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