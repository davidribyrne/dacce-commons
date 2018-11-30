package space.dcce.commons.netaddr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import space.dcce.commons.general.ArrayUtils;


public class IPUtils
{

	private IPUtils()
	{
	}


	private final static String RANGE_IN_OCTETS_IPV4_REGEX = "^(\\d{1,3}(-\\d{1,3})?\\.){3}\\d{1,3}(-\\d{1,3})?$";
	private final static String START_END_RANGE_IPV4_REGEX = "^(\\d{1,3}\\.){3}\\d{1,3}-(\\d{1,3}\\.){3}\\d{1,3}$";


	public static String bytesToString(byte[] address) throws InvalidIPAddressFormatException
	{
		if (address.length == 4)
		{
			return IP4Utils.bytesToString(address);
		}

		if (address.length == 16)
		{
			return IP6Utils.bytesToString(address);
		}

		throw new InvalidIPAddressFormatException("Invalid address length");
	}


	private static boolean matchesRangeInOctets(String address)
	{
		if (address.contains("-") && address.matches(RANGE_IN_OCTETS_IPV4_REGEX))
		{
			return true;
		}
		return false;
	}


	private static boolean matchesStartEndRange(String address)
	{
		if (address.contains("-") && address.matches(START_END_RANGE_IPV4_REGEX))
		{
			return true;
		}
		return false;
	}


	public static SimpleInetAddress fromString(String address) throws InvalidIPAddressFormatException
	{
		return SimpleInetAddress.getByAddress(IP4Utils.stringToBytes(address));
	}


	public static Collection<SimpleInetAddress> parseAddressBlock(String addressBlock) throws InvalidIPAddressFormatException
	{
		if (matchesRangeInOctets(addressBlock))
		{
			return handleRangeOctets(addressBlock);
		}
		if (matchesStartEndRange(addressBlock))
		{
			return parseStartEndRange(addressBlock);
		}
		if (matchesCidr(addressBlock))
		{
			return parseCidr(addressBlock);
		}
		if (matchesSingleAddress(addressBlock))
		{
			return Collections.singletonList(fromString(addressBlock));
		}
		throw new InvalidIPAddressFormatException("Invalid IP address format: " + addressBlock);
	}
	
	

	public static boolean validateAddressBlock(String addressBlock) 
	{
		if (matchesRangeInOctets(addressBlock))
		{
			return validateRangeInOctets(addressBlock);
		}
		if (matchesStartEndRange(addressBlock))
		{
			return validateStartEndRange(addressBlock);
		}
		if (matchesCidr(addressBlock))
		{
			return validateCidr(addressBlock);
		}
		if (matchesSingleAddress(addressBlock))
		{
			return validateSingleAddress(addressBlock);
		}
		
		return false;
	}

	public static boolean validateSingleAddress(String address)
	{
		try
		{
			IP4Utils.stringToBytes(address);
		}
		catch (InvalidIPAddressFormatException e)
		{
			return false;
		}
		return true;
	}

	private static boolean matchesSingleAddress(String address)
	{
		return address.matches("^(\\d{1,3}\\.){3}\\d{1,3}$");
	}


	private static boolean validateStartEndRange(String range)
	{
		String ends[] = range.split("-");
		long start;
		long end;
		try
		{
			start = IP4Utils.stringToDecimal(ends[0]);
			end = IP4Utils.stringToDecimal(ends[1]);
		}
		catch (InvalidIPAddressFormatException e)
		{
			return false;
		}
		if (start > end)
		{
			return false;
		}
		return true;
	}


	private static Collection<SimpleInetAddress> parseStartEndRange(String range) throws InvalidIPAddressFormatException
	{
		List<SimpleInetAddress> addresses = new ArrayList<SimpleInetAddress>();

		String ends[] = range.split("-");
		int start = IP4Utils.stringToDecimal(ends[0]);
		int end = IP4Utils.stringToDecimal(ends[1]);
		if (start > end)
		{
			throw new InvalidIPAddressFormatException("Start must be less than end in IP address range");
		}

		for (int i = start; i <= end; i++)
		{
			addresses.add(IP4Utils.decimalToAddress(start + i));
		}
		return addresses;

	}

	private static boolean validateRangeInOctets(String range) 
	{
		String[] octets = range.split("\\.");
		if (octets.length != 4)
		{
			return false;
		}

		for(String octet: octets)
		{
			String parts[] = octet.split("-");
			int start;
			int end;
			start = Integer.parseInt(parts[0]);
			if (parts.length == 2)
			{
				end = Integer.parseInt(parts[1]);
			}
			else if (parts.length == 1)
			{
				end = start;
			}
			else
			{
				return false;
			}
			

			if ((start > 255) || (end > 255) || (end < start))
			{
				return false;
			}
		}
		return true;
	}

	private static Collection<SimpleInetAddress> handleRangeOctets(String range) throws InvalidIPAddressFormatException
	{
		List<String> octets = new ArrayList<String>(Arrays.asList(range.split("\\.")));
		List<SimpleInetAddress> addresses = new ArrayList<SimpleInetAddress>();

		handleOctet(range, new byte[0], octets, addresses);

		return addresses;
	}



	private static void handleOctet(String range, byte base[], List<String> octets, List<SimpleInetAddress> addresses) throws InvalidIPAddressFormatException
	{

		String parts[] = octets.get(0).split("-");
		int start;
		int end;
		start = Integer.parseInt(parts[0]);
		if (parts.length == 2)
		{
			end = Integer.parseInt(parts[1]);
		}
		else if (parts.length == 1)
		{
			end = start;
		}
		else
		{
			throw new InvalidIPAddressFormatException("Invalid range format (" + range + ")");
		}

		if ((start > 255) || (end > 255) || (end < start))
		{
			throw new InvalidIPAddressFormatException("Invalid range format (" + range + ")");
		}

		for (int i = start; i <= end; i++)
		{
			byte newBase[] = ArrayUtils.append(base, (byte) i);
			if (octets.size() > 1)
			{
				handleOctet(range, newBase, octets.subList(1, octets.size()), addresses);
			}
			else
			{
				addresses.add(SimpleInetAddress.getByAddress(newBase));
			}
		}
	}


	private static boolean matchesCidr(String address)
	{
		return address.matches("^(\\d{1,3}\\.){3}\\d{1,3}/\\d{1,2}$");
	}

	private static boolean validateCidr(String cidrText) 
	{
		if (!matchesCidr(cidrText))
		{
			return false;
		}

		String parts[] = cidrText.split("/");
		int mask = Integer.parseInt(parts[1]);
		if ((mask > 32) || (mask < 1))
		{
			return false;
		}

		int count = (int) Math.pow(2, (32 - mask));
		long base;
		try
		{
			base = IP4Utils.stringToDecimal(parts[0]);
		}
		catch (InvalidIPAddressFormatException e)
		{
			return false;
		}
		if ((base % count) != 0)
		{
			return false;
		}

		return true;
	}

	
	private static Collection<SimpleInetAddress> parseCidr(String cidrText) throws InvalidIPAddressFormatException
	{
		if (!validateCidr(cidrText))
		{
			throw new InvalidIPAddressFormatException("Invalid CIDR format (" + cidrText + ")");
		}

		String parts[] = cidrText.split("/");
		int mask = Integer.parseInt(parts[1]);

		int count = (int) Math.pow(2, (32 - mask));
		int base = IP4Utils.stringToDecimal(parts[0]);
		if ((base % count) != 0)
		{
			throw new InvalidIPAddressFormatException("Base address is not valid for netmask");
		}

		List<SimpleInetAddress> addresses = new ArrayList<SimpleInetAddress>(count);
		for (int i = 0; i < count; i++)
		{
			addresses.add(IP4Utils.decimalToAddress(base + i));
		}
		return addresses;
	}

}
