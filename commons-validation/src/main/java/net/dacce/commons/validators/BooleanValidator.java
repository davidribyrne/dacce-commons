package net.dacce.commons.validators;

import java.util.*;

import net.dacce.commons.general.BooleanFormatException;
import net.dacce.commons.general.BooleanUtils;

public class BooleanValidator implements Validator
{


	/**
	 * 
	 * @param strict Only accept true/false, otherwise accepts true/false on/off yes/no
	 */
	public BooleanValidator(boolean strict)
	{
	}


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
