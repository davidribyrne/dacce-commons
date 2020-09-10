package space.dcce.commons.dns.exceptions;


// TODO: Auto-generated Javadoc
/**
 * The Class DnsResponseTimeoutException.
 */
public class DnsResponseTimeoutException extends DnsClientException
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4906939264121357107L;


	/**
	 * Instantiates a new dns response timeout exception.
	 */
	public DnsResponseTimeoutException()
	{
	}


	/**
	 * Instantiates a new dns response timeout exception.
	 *
	 * @param message the message
	 */
	public DnsResponseTimeoutException(String message)
	{
		super(message);
	}


	/**
	 * Instantiates a new dns response timeout exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public DnsResponseTimeoutException(String message, Throwable cause)
	{
		super(message, cause);
	}


	/**
	 * Instantiates a new dns response timeout exception.
	 *
	 * @param cause the cause
	 */
	public DnsResponseTimeoutException(Throwable cause)
	{
		super(cause);
	}
	
}
