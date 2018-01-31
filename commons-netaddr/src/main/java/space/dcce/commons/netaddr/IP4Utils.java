package space.dcce.commons.netaddr;


public class IP4Utils
{
	private IP4Utils()
	{

	}

	public static final String SIMPLE_ADDRESS_REGEX = "^(\\d{1,3}\\.){3}\\d{1,3}$";


	public static String bytesToString(byte[] address) throws InvalidIPAddressFormatException
	{
		if (address.length == 4)
		{
			return (address[0] & 0xFF) + "." + (address[1] & 0xFF) + "." + (address[2] & 0xFF) + "." + (address[3] & 0xFF);
		}
		throw new InvalidIPAddressFormatException("Invalid address length.");
	}


	public static long stringToDecimal(String address) throws InvalidIPAddressFormatException
	{
		return intToDecimal(octetsToInt(stringToBytes(address)));
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


	public static int octetsToInt(byte address[]) throws InvalidIPAddressFormatException
	{
		if (address.length != 4)
		{
			throw new InvalidIPAddressFormatException("Invalid IPv4 address");
		}

		int i = 0;
		int pos = 3;
		for (byte octet : address)
		{
			i += Math.pow(256, pos--) * (octet & 0xFF);
		}
		return i;
	}


	public static byte[] intToOctets(int address)
	{
		byte[] arrayOfByte = new byte[4];
		arrayOfByte[0] = ((byte) ((address >>> 24) & 0xFF));
		arrayOfByte[1] = ((byte) ((address >>> 16) & 0xFF));
		arrayOfByte[2] = ((byte) ((address >>> 8) & 0xFF));
		arrayOfByte[3] = ((byte) (address & 0xFF));
		return arrayOfByte;
	}


	public static byte[] decimalToOctets(long address)
	{
		return intToOctets((int) (address));
	}


	public static long intToDecimal(int address)
	{
		return 0x00000000ffffffffL & address;
	}


	public static SimpleInetAddress decimalToAddress(long address) throws InvalidIPAddressFormatException
	{
		return SimpleInetAddress.getByAddress(decimalToOctets(address));
	}


	public static boolean isValidIPv4Address(String inet4Address)
	{
		try
		{
			stringToBytes(inet4Address);
		}
		catch (InvalidIPAddressFormatException e)
		{
			return false;
		}
		return true;
	}

}
