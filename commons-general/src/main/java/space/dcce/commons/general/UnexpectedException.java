package space.dcce.commons.general;


// TODO: Auto-generated Javadoc
/**
 * The Class UnexpectedException.
 * A runtime wrapper for exceptions that should be impossible
 */
public class UnexpectedException extends RuntimeException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new unexpected exception.
	 */
	public UnexpectedException()
	{
	}


	/**
	 * Instantiates a new unexpected exception.
	 *
	 * @param paramString the param string
	 */
	public UnexpectedException(String paramString)
	{
		super(paramString);
	}


	/**
	 * Instantiates a new unexpected exception.
	 *
	 * @param paramThrowable the param throwable
	 */
	public UnexpectedException(Throwable paramThrowable)
	{
		super(paramThrowable);
	}


	/**
	 * Instantiates a new unexpected exception.
	 *
	 * @param paramString the param string
	 * @param paramThrowable the param throwable
	 */
	public UnexpectedException(String paramString, Throwable paramThrowable)
	{
		super(paramString, paramThrowable);
	}


	/**
	 * Instantiates a new unexpected exception.
	 *
	 * @param paramString the param string
	 * @param paramThrowable the param throwable
	 * @param paramBoolean1 the param boolean 1
	 * @param paramBoolean2 the param boolean 2
	 */
	public UnexpectedException(String paramString, Throwable paramThrowable, boolean paramBoolean1, boolean paramBoolean2)
	{
		super(paramString, paramThrowable, paramBoolean1, paramBoolean2);
	}

}
