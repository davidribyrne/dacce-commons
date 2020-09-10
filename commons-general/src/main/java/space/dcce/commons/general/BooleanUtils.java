package space.dcce.commons.general;

// TODO: Auto-generated Javadoc
/**
 * The Class BooleanUtils.
 */
public class BooleanUtils
{

	/**
	 * Friendly parse.
	 *
	 * @param value the value
	 * @return true, if successful
	 * @throws BooleanFormatException the boolean format exception
	 */
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
