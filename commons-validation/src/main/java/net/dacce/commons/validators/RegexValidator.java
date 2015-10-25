package net.dacce.commons.validators;

import java.util.regex.Pattern;


public class RegexValidator implements Validator
{


	private Pattern pattern;

	public RegexValidator(String pattern)
	{
		this(pattern, 0);
	}

	public RegexValidator(String pattern, int flags)
	{
		this.pattern = Pattern.compile(pattern, flags);
	}


	@Override
	public void validate(String value) throws ValidationException
	{
		if (!pattern.matcher(value).matches())
		{
			throw new ValidationException("'" + value + "' does not match pattern '" + pattern.pattern() + "'.");
		}
	}
}
