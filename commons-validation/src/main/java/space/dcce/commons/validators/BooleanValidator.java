package space.dcce.commons.validators;

import space.dcce.commons.general.BooleanFormatException;
import space.dcce.commons.general.BooleanUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class BooleanValidator.
 */
public class BooleanValidator implements Validator
{


	/**
	 * Instantiates a new boolean validator.
	 *
	 * @param strict Only accept true/false, otherwise accepts true/false on/off yes/no
	 */
	public BooleanValidator(boolean strict)
	{
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
		try
		{
			BooleanUtils.friendlyParse(value);
		}
		catch (BooleanFormatException e)
		{
			throw new ValidationException(e.getLocalizedMessage(), e);
		}
	}
}
