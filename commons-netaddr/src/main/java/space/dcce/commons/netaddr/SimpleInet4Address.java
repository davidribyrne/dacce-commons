package space.dcce.commons.netaddr;

import space.dcce.commons.general.UnexpectedException;

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

	public long toLong()
	{
		try
		{
			return IP4Utils.bytesToLong(address);
		}
		catch (InvalidIPAddressFormatException e)
		{
			throw new UnexpectedException(e);
		}
	}
	
	/**
	 * 
	 * @param mask The number of bits in the subnet mask
	 * @return Network address based on mask
	 */
	@Override
	public SimpleInet4Address getNetworkAddress(int mask)
	{
		if (mask < 0 || mask > 32)
		{
			throw new IllegalArgumentException("Mask must be from 0-32, inclusive");
		}
		
		try
		{
			long i = IP4Utils.bytesToLong(address);
			long subnet = 0xFFFFFFFF << (32-mask);
			long ii = i & subnet;
			return IP4Utils.longToAddress(ii);
		}
		catch (InvalidIPAddressFormatException e)
		{
			throw new UnexpectedException(e);
		}
	}


	@Override
	public SimpleInet4Address getHostAddress(int mask)
	{
		if (mask < 0 || mask > 32)
		{
			throw new IllegalArgumentException("Mask must be from 0-32, inclusive");
		}
		
		try
		{
			long i = IP4Utils.bytesToLong(address);
			long subnet = 0xFFFFFFFF >>> mask;
			long ii = i & subnet;
			return IP4Utils.longToAddress(ii);
		}
		catch (InvalidIPAddressFormatException e)
		{
			throw new UnexpectedException(e);
		}
	}


	@Override
	public SimpleInet4Address addressAddition(long increment) throws InvalidIPAddressFormatException
	{
		return IP4Utils.longToAddress(toLong() + increment);
	}


}
