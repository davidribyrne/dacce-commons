package space.dcce.commons.validators;


// TODO: Auto-generated Javadoc
/**
 * The Class NumericValidator.
 */
public class NumericValidator implements Validator
{

	/** The floating. */
	private boolean floating;
	
	/** The max. */
	private double max;
	
	/** The min. */
	private double min;

	/**
	 * Validate.
	 *
	 * @param value the value
	 * @throws ValidationException the validation exception
	 */
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


	/**
	 * Instantiates a new numeric validator.
	 *
	 * @param floating the floating
	 * @param min the min
	 * @param max the max
	 */
	public NumericValidator(boolean floating, double min, double max)
	{
		this.floating = floating;
		this.max = max;
		this.min = min;
	}

}
