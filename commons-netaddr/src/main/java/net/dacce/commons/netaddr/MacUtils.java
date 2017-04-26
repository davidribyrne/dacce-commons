package net.dacce.commons.netaddr;

import java.util.regex.Pattern;


public class MacUtils
{

	private MacUtils()
	{
	}

	private static final Pattern macPattern = Pattern.compile("^(?:[0-9a-fA-F]{2}[:\\- \\.]){5}[0-9a-fA-F]{2}$");

	
	public static String getHexString(byte[] address)
	{
		if (address == null)
			return "";
		
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (byte b: address)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(':');
			}
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public static byte[] getMacFromString(String address) throws IllegalArgumentException
	{
		byte[] bytes = new byte[6];
		if (macPattern.matcher(address).matches())
		{
			int index = 0;
			for (String section : address.split("[:\\- \\.]"))
			{
				bytes[index++] = (byte) Integer.parseInt(section, 16);
			}
		}
		else
		{
			throw new IllegalArgumentException("Invalid MAC address: " + address);
		}
		return bytes;

	}

}
