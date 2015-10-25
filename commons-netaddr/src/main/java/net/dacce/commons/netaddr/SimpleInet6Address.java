package net.dacce.commons.netaddr;



public class SimpleInet6Address extends SimpleInetAddress
{

	protected SimpleInet6Address(byte[] address) throws InvalidIPAddressFormatException
	{
		super(address);
		if (address.length != 16)
		{
			throw new InvalidIPAddressFormatException("IPv6 address can't be " + address.length + " bytes long.");
		}
	}

}
