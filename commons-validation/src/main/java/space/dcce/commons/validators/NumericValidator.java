package space.dcce.commons.validators;


public class NumericValidator implements Validator
{

	private boolean floating;
	private double max;
	private double min;

	public void validate(String value) throws ValidationException
	{
		double d;
		try
		{
			d = Double.parseDouble(value);
		}
		catch (NumberFormatException e)
		{
			throw new ValidationException("The value '" + value + "' is not a valid number.", e);
		}
		if (!floating && d != Math.floor(d))
		{
			throw new ValidationException("The value '" + value + "' is not an integer.");
		}
		if (d < min)
		{
			throw new ValidationException("The value '" + value + "' is too small.");
		}
		if (d > max)
		{
			throw new ValidationException("The value '" + value + "' is too large.");
		}
	}


	public NumericValidator(boolean floating, double min, double max)
	{
		this.floating = floating;
		this.max = max;
		this.min = min;
	}

}
