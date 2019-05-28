package space.dcce.commons.netaddr;


public class IPUtils
{

	private IPUtils()
	{
	}



	public static SimpleInetAddress fromString(String address) throws InvalidIPAddressFormatException
	{
		return SimpleInetAddress.getByAddress(IP4Utils.stringToBytes(address));
	}


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



}
