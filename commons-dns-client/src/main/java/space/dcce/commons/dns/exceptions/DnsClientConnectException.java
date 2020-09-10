package space.dcce.commons.dns.exceptions;


// TODO: Auto-generated Javadoc
/**
 * The Class DnsClientConnectException.
 */
public class DnsClientConnectException extends DnsClientException
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new dns client connect exception.
	 */
	public DnsClientConnectException()
	{
	}


	/**
	 * Instantiates a new dns client connect exception.
	 *
	 * @param message the message
	 */
	public DnsClientConnectException(String message)
	{
		super(message);
	}


	/**
	 * Instantiates a new dns client connect exception.
	 *
	 * @param cause the cause
	 */
	public DnsClientConnectException(Throwable cause)
	{
		super(cause);
	}


	/**
	 * Instantiates a new dns client connect exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public DnsClientConnectException(String message, Throwable cause)
	{
		super(message, cause);
	}


	/**
	 * Instantiates a new dns client connect exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public DnsClientConnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
