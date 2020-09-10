package space.dcce.commons.validators;



// TODO: Auto-generated Javadoc
/**
 * The Class NumericListValidator.
 */
public class NumericListValidator extends RegexValidator
{
	
	/** The numeric validator. */
	private NumericValidator numericValidator;
	
	/** The delimiter. */
	private String delimiter;
	
	/**
	 * Instantiates a new numeric list validator.
	 *
	 * @param delimiterPattern the delimiter pattern
	 * @param floating the floating
	 * @param min the min
	 * @param max the max
	 */
	public NumericListValidator(String delimiterPattern, boolean floating, double min, double max)
	{
		super("^(?:\\d+" + delimiterPattern + ")*\\d+$");
		this.delimiter = delimiterPattern;
		numericValidator = new NumericValidator(floating, min, max);
	}

	/**
	 * Validate.
	 *
	 * @param value the value
	 * @throws ValidationException the validation exception
	 */
	@Override
	public void validate(String value) throws ValidationException
	{
		super.validate(value);
		for(String component: value.split(delimiter))
		{
			numericValidator.validate(component);
		}
	}
	

}
