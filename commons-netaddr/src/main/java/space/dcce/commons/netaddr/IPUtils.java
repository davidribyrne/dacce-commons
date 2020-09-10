package space.dcce.commons.netaddr;


// TODO: Auto-generated Javadoc
/**
 * The Class IPUtils.
 */
public class IPUtils
{

	/**
	 * Instantiates a new IP utils.
	 */
	private IPUtils()
	{
	}



	/**
	 * From string.
	 *
	 * @param address the address
	 * @return the simple inet address
	 * @throws InvalidIPAddressFormatException the invalid IP address format exception
	 */
	public static SimpleInetAddress fromString(String address) throws InvalidIPAddressFormatException
	{
		return SimpleInetAddress.getByAddress(IP4Utils.stringToBytes(address));
	}


	/**
	 * Bytes to string.
	 *
	 * @param address the address
	 * @return the string
	 * @throws InvalidIPAddressFormatException the invalid IP address format exception
	 */
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
