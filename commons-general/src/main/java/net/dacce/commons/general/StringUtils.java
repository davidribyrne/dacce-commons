package net.dacce.commons.general;

import java.util.Arrays;


public class StringUtils
{

	private StringUtils()
	{
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
