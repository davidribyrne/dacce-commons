package net.dacce.commons.netaddr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.dacce.commons.general.ArrayUtils;


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


	private static boolean matchesRange(String address)
	{
		if (address.contains("-") &&
				(address.matches(RANGE_IN_OCTETS_IPV4_REGEX) ||
				address.matches(START_END_RANGE_IPV4_REGEX)))
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
		if (matchesRange(addressBlock))
		{
			return parseAddressRange(addressBlock);
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


	private static boolean matchesSingleAddress(String address)
	{
		return address.matches("^(\\d{1,3}\\.){3}\\d{1,3}$");
	}


	private static Collection<SimpleInetAddress> parseAddressRange(String range) throws InvalidIPAddressFormatException
	{
		List<SimpleInetAddress> addresses = new ArrayList<SimpleInetAddress>();

		if (range.matches(RANGE_IN_OCTETS_IPV4_REGEX))
		{
			List<String> octets = new ArrayList<String>(Arrays.asList(range.split("\\.")));
			handleRangeOctets(new byte[0], octets, addresses);
		}
		else if (range.matches(START_END_RANGE_IPV4_REGEX))
		{
			String ends[] = range.split("-");
			long start = IP4Utils.stringToDecimal(ends[0]);
			long end = IP4Utils.stringToDecimal(ends[1]);
			if (start > end)
			{
				throw new IllegalArgumentException("Invalid range: end less than start");
			}
			for (long i = start; i <= end; i++)
			{
				addresses.add(IP4Utils.decimalToAddress(start + i));
			}
		}
		else
		{
			throw new IllegalArgumentException("Invalid range format");
		}
		return addresses;

	}


	private static void handleRangeOctets(byte base[], List<String> octets, List<SimpleInetAddress> addresses) throws InvalidIPAddressFormatException
	{
		String parts[] = octets.remove(0).split("-");
		int start;
		int end;
		start = Integer.parseInt(parts[0]);
		if (parts.length == 2)
		{
			end = Integer.parseInt(parts[1]);
		}
		else
		{
			end = start;
		}

		if ((start > 255) || (end > 255) || (end < start))
		{
			throw new IllegalArgumentException("Invalid range format");
		}

		for (byte i = (byte) start; i <= end; i++)
		{
			byte newBase[] = ArrayUtils.append(base, i);
			if (octets.size() > 0)
			{
				handleRangeOctets(newBase, octets, addresses);
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


	private static Collection<SimpleInetAddress> parseCidr(String cidrText) throws InvalidIPAddressFormatException
	{
		if (!matchesCidr(cidrText))
		{
			throw new IllegalArgumentException("Invalid CIDR format");
		}

		String parts[] = cidrText.split("/");
		int mask = Integer.parseInt(parts[1]);
		if ((mask > 32) || (mask < 1))
		{
			throw new IllegalArgumentException("Invalid netmask");
		}

		int count = (int) Math.pow(2, (32 - mask));
		long base = IP4Utils.stringToDecimal(parts[0]);
		if ((base % count) != 0)
		{
			throw new IllegalArgumentException("Base address is not valid for netmask");
		}

		List<SimpleInetAddress> addresses = new ArrayList<SimpleInetAddress>(count);
		for (int i = 0; i < count; i++)
		{
			addresses.add(IP4Utils.decimalToAddress(base + i));
		}
		return addresses;
	}

}
