package net.dacce.commons.netaddr;

import java.util.Arrays;
import org.apache.commons.lang3.builder.HashCodeBuilder;



public abstract class SimpleInetAddress
{
	protected final byte[] address;

	protected SimpleInetAddress(byte[] address)
	{
		this.address = address;
	}

	public static SimpleInetAddress getByAddress(byte[] address) throws InvalidIPAddressFormatException
	{
		if (address.length == 4)
		{
			return new SimpleInet4Address(address);
		}
		if (address.length == 16)
		{
			return new SimpleInet6Address(address);
		}
		throw new InvalidIPAddressFormatException("Invalid address length (" + address.length + ").");
	}

	public byte[] getAddress()
	{
		return address;
	}



	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(address).toHashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof SimpleInetAddress))
			return false;
		if (obj == this)
			return true;
		
		return Arrays.equals(address, ((SimpleInetAddress) obj).address);
	}

	@Override
	public String toString()
	{
		try
		{
			return IPUtils.bytesToString(getAddress());
		}
		catch (InvalidIPAddressFormatException e)
		{
			throw new IllegalStateException("This never should have happened: " + e.getLocalizedMessage(), e);
		}
	}

}
