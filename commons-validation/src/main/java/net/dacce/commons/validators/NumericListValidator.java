package net.dacce.commons.validators;



public class NumericListValidator extends RegexValidator
{
	private NumericValidator numericValidator;
	private String delimiter;
	
	public NumericListValidator(String delimiterPattern, boolean floating, double min, double max)
	{
		super("^(?:\\d+" + delimiterPattern + ")*\\d+$");
		this.delimiter = delimiterPattern;
		numericValidator = new NumericValidator(floating, min, max);
	}

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
