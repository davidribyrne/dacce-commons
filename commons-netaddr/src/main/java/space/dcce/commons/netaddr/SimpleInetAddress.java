package space.dcce.commons.netaddr;

import java.util.Arrays;

import org.apache.commons.lang3.builder.HashCodeBuilder;



// TODO: Auto-generated Javadoc
/**
 * The Class SimpleInetAddress.
 */
public abstract class SimpleInetAddress
{
	
	/** The address. */
	protected final byte[] address;

	/**
	 * Instantiates a new simple inet address.
	 *
	 * @param address the address
	 */
	protected SimpleInetAddress(byte[] address)
	{
		this.address = address;
	}

	/**
	 * Gets the by address.
	 *
	 * @param address the address
	 * @return the by address
	 * @throws InvalidIPAddressFormatException the invalid IP address format exception
	 */
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

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public byte[] getAddress()
	{
		return address;
	}


	/**
	 * Gets the network address.
	 *
	 * @param mask The number of bits in the subnet mask
	 * @return Network address based on mask
	 */
	public abstract SimpleInetAddress getNetworkAddress(int mask);

	
	/**
	 * Gets the host address.
	 *
	 * @param mask The number of bits in the subnet mask
	 * @return Host address based on mask
	 */
	public abstract SimpleInetAddress getHostAddress(int mask);

	/**
	 * Address addition.
	 *
	 * @param increment Positive or negative number to add to IP address
	 * @return Result of increment
	 * @throws InvalidIPAddressFormatException If increment makes address beyond range
	 */
	public abstract SimpleInetAddress addressAddition(long increment) throws InvalidIPAddressFormatException;
	
	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(address).toHashCode();
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof SimpleInetAddress))
			return false;
		if (obj == this)
			return true;
		
		return Arrays.equals(address, ((SimpleInetAddress) obj).address);
	}

	
	/**
	 * To string.
	 *
	 * @return the string
	 */
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
