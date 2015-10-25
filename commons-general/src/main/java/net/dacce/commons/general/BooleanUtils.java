package net.dacce.commons.general;

public class BooleanUtils
{

	public static boolean friendlyParse(String value) throws BooleanFormatException
	{
		String s = value.toLowerCase();
		if (s.equals("true") || s.equals("yes") || s.equals("on"))
		{
			return true;
		}
		if (s.equals("false") || s.equals("no") || s.equals("off"))
		{
			return false;
		}
		throw new BooleanFormatException(value + " is not a valid boolean value. Try true/yes/on or false/no/off.");
	}

}
