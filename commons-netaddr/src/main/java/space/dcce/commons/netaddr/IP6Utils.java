package space.dcce.commons.netaddr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class IP6Utils
{


	private IP6Utils()
	{

	}


	/**
	 * Based on Inet6Address.numericToTextFormat()
	 * 
	 * @param address
	 * @return
	 * @throws InvalidIPAddressFormatException
	 */
	public static String bytesToString(byte[] address) throws InvalidIPAddressFormatException
	{
		if (address.length == 16)
		{
			StringBuilder sb = new StringBuilder(39);
			for (int i = 0; i < 8; i++)
			{
				sb.append(Integer.toHexString(((address[i << 1] << 8) & 0xff00)
						| (address[(i << 1) + 1] & 0xff)));
				if (i < 7)
				{
					sb.append(":");
				}
			}
			return sb.toString();
		}
		throw new InvalidIPAddressFormatException("Invalid IPv6 address length.");
	}

	/**
	 * From Apache Commons Validator
	 * @param inet6Address
	 * @return
	 */
	public static boolean isValidInet6Address(String inet6Address)
	{
		boolean containsCompressedZeroes = inet6Address.contains("::");
		if (containsCompressedZeroes && (inet6Address.indexOf("::") != inet6Address.lastIndexOf("::")))
		{
			return false;
		}
		if ((inet6Address.startsWith(":") && !inet6Address.startsWith("::"))
				|| (inet6Address.endsWith(":") && !inet6Address.endsWith("::")))
		{
			return false;
		}
		String[] octets = inet6Address.split(":");
		if (containsCompressedZeroes)
		{
			List<String> octetList = new ArrayList<String>(Arrays.asList(octets));
			if (inet6Address.endsWith("::"))
			{
				// String.split() drops ending empty segments
				octetList.add("");
			}
			else if (inet6Address.startsWith("::") && !octetList.isEmpty())
			{
				octetList.remove(0);
			}
			octets = octetList.toArray(new String[octetList.size()]);
		}
		if (octets.length > 8)
		{
			return false;
		}
		int validOctets = 0;
		int emptyOctets = 0;
		for (int index = 0; index < octets.length; index++)
		{
			String octet = (String) octets[index];
			if (octet.length() == 0)
			{
				emptyOctets++;
				if (emptyOctets > 1)
				{
					return false;
				}
			}
			else
			{
				emptyOctets = 0;
				if (octet.contains("."))
				{ // contains is Java 1.5+
					if (!inet6Address.endsWith(octet))
					{
						return false;
					}
					if (index > octets.length - 1 || index > 6)
					{
						// IPV4 occupies last two octets
						return false;
					}
					if (!IP4Utils.isValidIPv4Address(octet))
					{
						return false;
					}
					validOctets += 2;
					continue;
				}
				if (octet.length() > 4)
				{
					return false;
				}
				int octetInt = 0;
				try
				{
					octetInt = Integer.valueOf(octet, 16).intValue();
				}
				catch (NumberFormatException e)
				{
					return false;
				}
				if (octetInt < 0 || octetInt > 0xffff)
				{
					return false;
				}
			}
			validOctets++;
		}
		if (validOctets < 8 && !containsCompressedZeroes)
		{
			return false;
		}
		return true;
	}


}
