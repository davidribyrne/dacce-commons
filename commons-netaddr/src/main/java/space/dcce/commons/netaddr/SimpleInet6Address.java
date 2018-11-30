package space.dcce.commons.netaddr;

import space.dcce.commons.general.NotImplementedException;

public class SimpleInet6Address extends SimpleInetAddress
{

	public SimpleInet6Address(byte[] address) throws InvalidIPAddressFormatException
	{
		super(address);
		if (address.length != 16)
		{
			throw new InvalidIPAddressFormatException("IPv6 address can't be " + address.length + " bytes long.");
		}
	}

	@Override
	public SimpleInetAddress getNetworkAddress(int mask)
	{
		throw new NotImplementedException();
	}

	@Override
	public SimpleInetAddress getHostAddress(int mask)
	{
		throw new NotImplementedException();
	}

	@Override
	public SimpleInetAddress addressAddition(int increment)
	{
		throw new NotImplementedException();
	}

}
