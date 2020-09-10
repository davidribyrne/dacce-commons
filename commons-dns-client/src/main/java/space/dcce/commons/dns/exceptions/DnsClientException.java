package space.dcce.commons.dns.exceptions;


// TODO: Auto-generated Javadoc
/**
 * The Class DnsClientException.
 */
public class DnsClientException extends Exception
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new dns client exception.
	 */
	public DnsClientException()
	{
	}


	/**
	 * Instantiates a new dns client exception.
	 *
	 * @param message the message
	 */
	public DnsClientException(String message)
	{
		super(message);
	}


	/**
	 * Instantiates a new dns client exception.
	 *
	 * @param cause the cause
	 */
	public DnsClientException(Throwable cause)
	{
		super(cause);
	}


	/**
	 * Instantiates a new dns client exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public DnsClientException(String message, Throwable cause)
	{
		super(message, cause);
	}


	/**
	 * Instantiates a new dns client exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public DnsClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
