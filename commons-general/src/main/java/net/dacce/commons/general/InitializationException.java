/**
 *
 */
package net.dacce.commons.general;


/**
 * @author dbyrne
 *
 */
public class InitializationException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *
	 */
	public InitializationException()
	{
	}


	/**
	 * @param paramString
	 */
	public InitializationException(String paramString)
	{
		super(paramString);
	}


	/**
	 * @param paramThrowable
	 */
	public InitializationException(Throwable paramThrowable)
	{
		super(paramThrowable);
	}


	/**
	 * @param paramString
	 * @param paramThrowable
	 */
	public InitializationException(String paramString, Throwable paramThrowable)
	{
		super(paramString, paramThrowable);
	}


	/**
	 * @param paramString
	 * @param paramThrowable
	 * @param paramBoolean1
	 * @param paramBoolean2
	 */
	public InitializationException(String paramString, Throwable paramThrowable, boolean paramBoolean1, boolean paramBoolean2)
	{
		super(paramString, paramThrowable, paramBoolean1, paramBoolean2);
	}

}
