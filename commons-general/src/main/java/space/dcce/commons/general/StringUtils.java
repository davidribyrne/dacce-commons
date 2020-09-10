package space.dcce.commons.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Class StringUtils.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{

	/**
	 * Instantiates a new string utils.
	 */
	private StringUtils()
	{
	}


	/**
	 * Trims strings and removes all empty or null items.
	 *
	 * @param strings the strings
	 * @return the list
	 */
	public static List<String> trim(List<String> strings)
	{
		List<String> newList = new ArrayList<String>();
		for(String string: strings)
		{
			if (string == null)
				continue;
			String newString = string.trim();
			if (newString.isEmpty())
				continue;
			newList.add(newString);
		}
		return newList;
	}
	
	/**
	 * Checks if is empty or null.
	 *
	 * @param string the string
	 * @return true, if is empty or null
	 */
	public static boolean isEmptyOrNull(String string)
	{
		return (string == null) || string.isEmpty();
	}
	
	/**
	 * Indent text.
	 *
	 * @param count the count
	 * @param tabs the tabs
	 * @param text the text
	 * @return the string
	 */
	public static String indentText(int count, boolean tabs, String text)
	{
		String prefix = fillString(tabs ? '\t' : ' ', count);
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String line: text.split("\n"))
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append("\n");
			}
			sb.append(prefix);
			sb.append(line);
		}
		return sb.toString();
	}
	
	/**
	 * Fill string.
	 *
	 * @param c the c
	 * @param count the count
	 * @return the string
	 */
	public static String fillString(char c, int count)
	{
		char[] a = new char[count];
		Arrays.fill(a, c);
		return new String(a);
	}
}
