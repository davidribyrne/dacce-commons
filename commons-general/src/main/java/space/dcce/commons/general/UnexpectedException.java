package space.dcce.commons.general;


/**
 * The Class UnexpectedException.
 * A runtime wrapper for exceptions that should be impossible
 */
public class UnexpectedException extends RuntimeException
{

	private static final long serialVersionUID = 1L;


	public UnexpectedException()
	{
	}


	public UnexpectedException(String paramString)
	{
		super(paramString);
	}


	public UnexpectedException(Throwable paramThrowable)
	{
		super(paramThrowable);
	}


	public UnexpectedException(String paramString, Throwable paramThrowable)
	{
		super(paramString, paramThrowable);
	}


	public UnexpectedException(String paramString, Throwable paramThrowable, boolean paramBoolean1, boolean paramBoolean2)
	{
		super(paramString, paramThrowable, paramBoolean1, paramBoolean2);
	}

}
