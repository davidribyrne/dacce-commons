package space.dcce.commons.validators;

import java.util.regex.Pattern;


// TODO: Auto-generated Javadoc
/**
 * The Class RegexValidator.
 */
public class RegexValidator implements Validator
{


	/** The pattern. */
	private Pattern pattern;

	/**
	 * Instantiates a new regex validator.
	 *
	 * @param pattern the pattern
	 */
	public RegexValidator(String pattern)
	{
		this(pattern, 0);
	}

	/**
	 * Instantiates a new regex validator.
	 *
	 * @param pattern the pattern
	 * @param flags the flags
	 */
	public RegexValidator(String pattern, int flags)
	{
		this.pattern = Pattern.compile(pattern, flags);
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
		if (!pattern.matcher(value).matches())
		{
			throw new ValidationException("'" + value + "' does not match pattern '" + pattern.pattern() + "'.");
		}
	}
}
