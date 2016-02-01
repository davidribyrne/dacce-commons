package net.dacce.commons.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StringUtils extends org.apache.commons.lang3.StringUtils
{

	private StringUtils()
	{
	}


	/**
	 * Trims strings and removes all empty or null items
	 * @param strings
	 * @return
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
	
	public static boolean isEmptyOrNull(String string)
	{
		return (string == null) || string.isEmpty();
	}
	
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
	
	public static String fillString(char c, int count)
	{
		char[] a = new char[count];
		Arrays.fill(a, c);
		return new String(a);
	}
}
