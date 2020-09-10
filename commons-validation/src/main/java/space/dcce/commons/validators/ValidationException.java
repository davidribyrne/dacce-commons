package space.dcce.commons.validators;


// TODO: Auto-generated Javadoc
/**
 * The Class ValidationException.
 */
public class ValidationException extends Exception
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 467903337038064731L;


	/**
	 * Instantiates a new validation exception.
	 *
	 * @param message the message
	 */
	public ValidationException(String message)
	{
		super(message);
	}



	/**
	 * Instantiates a new validation exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}



	/**
	 * Instantiates a new validation exception.
	 */
	public ValidationException()
	{
	}

}
