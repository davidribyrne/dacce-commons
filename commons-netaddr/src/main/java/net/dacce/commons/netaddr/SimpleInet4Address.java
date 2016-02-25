package net.dacce.commons.netaddr;


public class SimpleInet4Address extends SimpleInetAddress
{

	public SimpleInet4Address(byte[] address) throws InvalidIPAddressFormatException
	{
		super(address);
		if (address.length != 4)
		{
			throw new InvalidIPAddressFormatException("IPv4 address can't be " + address.length + " bytes long.");
		}
	}


}
