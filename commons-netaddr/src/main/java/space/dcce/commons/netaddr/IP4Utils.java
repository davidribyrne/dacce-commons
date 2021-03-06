package space.dcce.commons.netaddr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import space.dcce.commons.general.ArrayUtils;
import space.dcce.commons.general.Range;
import space.dcce.commons.general.UnexpectedException;


public class IP4Utils
{
	private IP4Utils()
	{
	}

	private final static String RANGE_IN_OCTETS_IPV4_REGEX = "^(\\d{1,3}(-\\d{1,3})?\\.){3}\\d{1,3}(-\\d{1,3})?$";
	private final static String START_END_RANGE_IPV4_REGEX = "^(\\d{1,3}\\.){3}\\d{1,3}-(\\d{1,3}\\.){3}\\d{1,3}$";

	public static final String SIMPLE_ADDRESS_REGEX = "^(\\d{1,3}\\.){3}\\d{1,3}$";


	public static String bytesToString(byte[] address) throws InvalidIPAddressFormatException
	{
		if (address.length == 4)
		{
			return (address[0] & 0xFF) + "." + (address[1] & 0xFF) + "." + (address[2] & 0xFF) + "." + (address[3] & 0xFF);
		}
		throw new InvalidIPAddressFormatException("Invalid address length.");
	}


	public static long stringToLong(String address) throws InvalidIPAddressFormatException
	{
		return bytesToLong(stringToBytes(address));
	}


	public static byte[] stringToBytes(String address) throws InvalidIPAddressFormatException
	{
		if (!address.matches(SIMPLE_ADDRESS_REGEX))
		{
			throw new InvalidIPAddressFormatException("Invalid IPv4 address");
		}
		String octets[] = address.split("\\.");
		byte bytes[] = new byte[4];
		for (int j = 0; j < octets.length; j++)
		{
			int i = Integer.parseInt(octets[j]);
			if (i > 255)
			{
				throw new InvalidIPAddressFormatException("Invalid IPv4 address");
			}
			bytes[j] = (byte) i;
		}
		return bytes;
	}


	public static int getNetMask(int maskBits)
	{
		return (1 << (maskBits + 1)) - 1;
	}


	public static long bytesToLong(byte address[]) throws InvalidIPAddressFormatException
	{
		if (address.length != 4)
		{
			throw new InvalidIPAddressFormatException("Invalid IPv4 address");
		}

		return (address[3] & 0xFF) | ((address[2] & 0xFF) << 8) | ((address[1] & 0xFF) << 16) | address[0] << 24;
	}


	public static byte[] longToBytes(long address)
	{
		byte[] arrayOfByte = new byte[4];
		arrayOfByte[0] = ((byte) ((address >>> 24) & 0xFF));
		arrayOfByte[1] = ((byte) ((address >>> 16) & 0xFF));
		arrayOfByte[2] = ((byte) ((address >>> 8) & 0xFF));
		arrayOfByte[3] = ((byte) (address & 0xFF));
		return arrayOfByte;
	}


	public static String longToString(long address)
	{
		try
		{
			return bytesToString(longToBytes(address));
		}
		catch (InvalidIPAddressFormatException e)
		{
			throw new UnexpectedException(e);
		}
	}




	public static SimpleInet4Address longToAddress(long address) throws InvalidIPAddressFormatException
	{
		return new SimpleInet4Address(longToBytes(address));
	}


	public static boolean isValidIPv4Address(String inet4Address)
	{
		try
		{
			stringToBytes(inet4Address);
		}
		catch (@SuppressWarnings("unused") InvalidIPAddressFormatException e)
		{
			return false;
		}
		return true;
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


	public static Collection<Range> parseAddressRange(String addressBlock) throws InvalidIPAddressFormatException
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
			long a = IP4Utils.stringToLong(addressBlock);
			return Collections.singletonList(new Range(a, a));
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
			start = IP4Utils.stringToLong(ends[0]);
			end = IP4Utils.stringToLong(ends[1]);
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


	private static Collection<Range> parseStartEndRange(String range) throws InvalidIPAddressFormatException
	{

		String ends[] = range.split("-");
		long start = IP4Utils.stringToLong(ends[0]);
		long end = IP4Utils.stringToLong(ends[1]);
		if (start > end)
		{
			throw new InvalidIPAddressFormatException("Start must be less than end in IP address range");
		}

		return Collections.singleton(new Range(start, end));
	}


	private static boolean validateRangeInOctets(String range)
	{
		String[] octets = range.split("\\.");
		if (octets.length != 4)
		{
			return false;
		}

		for (String octet : octets)
		{
			String parts[] = octet.split("-");
			long start;
			long end;
			start = Long.parseUnsignedLong(parts[0]);
			if (parts.length == 2)
			{
				end = Long.parseUnsignedLong(parts[1]);
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


	private static Collection<Range> handleRangeOctets(String range) throws InvalidIPAddressFormatException
	{
		List<String> octets = new ArrayList<String>(Arrays.asList(range.split("\\.")));
		List<Range> addresses = new ArrayList<Range>();

		handleOctet(range, new byte[0], octets, addresses);

		return addresses;
	}


	private static void handleOctet(String range, byte base[], List<String> octets, List<Range> addresses) throws InvalidIPAddressFormatException
	{

		String parts[] = octets.get(0).split("-");
		long start;
		long end;
		start = Long.parseUnsignedLong(parts[0]);
		if (parts.length == 2)
		{
			end = Long.parseUnsignedLong(parts[1]);
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


		if (octets.size() > 1)
		{
			for (long i = start; i <= end; i++)
			{
				byte newBase[] = ArrayUtils.append(base, (byte) i);
				handleOctet(range, newBase, octets.subList(1, octets.size()), addresses);
			}
		}
		else
		{
			long startAddress = IP4Utils.bytesToLong(ArrayUtils.append(base, (byte) start));
			long endAddress = IP4Utils.bytesToLong(ArrayUtils.append(base, (byte) end));

			addresses.add(new Range(startAddress, endAddress));
		}
	}


	private static boolean matchesCidr(String address)
	{
		return address.matches("^(\\d{1,3}\\.){3}\\d{1,3}/\\d{1,2}$");
	}


	public static boolean validateCidr(String cidrText)
	{
		if (!matchesCidr(cidrText))
		{
			return false;
		}

		String parts[] = cidrText.split("/");
		int mask = Integer.parseInt(parts[1]);
		if ((mask > 32) || (mask < 0))
		{
			return false;
		}

		long count = (long) Math.pow(2, (32 - mask));
		long base;
		try
		{
			base = IP4Utils.stringToLong(parts[0]);
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


	private static Collection<Range> parseCidr(String cidrText) throws InvalidIPAddressFormatException
	{
		if (!validateCidr(cidrText))
		{
			throw new InvalidIPAddressFormatException("Invalid CIDR format (" + cidrText + ")");
		}

		String parts[] = cidrText.split("/");
		int mask = Integer.parseInt(parts[1]);

		long count = (long) Math.pow(2, (32 - mask));
		long base = IP4Utils.stringToLong(parts[0]);
		if ((base % count) != 0)
		{
			throw new InvalidIPAddressFormatException("Base address is not valid for netmask");
		}

		return Collections.singleton(new Range(base, base + count - 1));
	}


	public static List<String> rangeToCIDRs(long start, long end)
	{
		if (end < start)
		{
			throw new IllegalArgumentException("Start must be less than or equal to end");
		}

		List<String> pairs = new ArrayList<String>();
		while (end >= start)
		{
			byte maxsize = 32;
			while (maxsize > 0)
			{
				int mask = bitsToMask(maxsize - 1);
				long maskedBase = start & mask;

				if (maskedBase != start)
				{
					break;
				}

				maxsize--;
			}
			double x = Math.log(end - start + 1) / Math.log(2);
			byte maxdiff = (byte) (32 - Math.floor(x));
			if (maxsize < maxdiff)
			{
				maxsize = maxdiff;
			}
			pairs.add(longToString(start) + "/" + maxsize);
			start += Math.pow(2, (32 - maxsize));
		}
		return pairs;
	}


	public static int bitsToMask(int bits)
	{
		return 0xFFFFFFFF << (32 - bits);
	}

}
