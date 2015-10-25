package net.dacce.commons.validators;


public class ValidationException extends Exception
{

	private static final long serialVersionUID = 467903337038064731L;


	public ValidationException(String message)
	{
		super(message);
	}



	public ValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}



	public ValidationException()
	{
	}

}
