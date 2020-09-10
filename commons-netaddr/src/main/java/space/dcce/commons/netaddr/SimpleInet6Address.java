package space.dcce.commons.netaddr;

import space.dcce.commons.general.NotImplementedException;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleInet6Address.
 */
public class SimpleInet6Address extends SimpleInetAddress
{

	/**
	 * Instantiates a new simple inet 6 address.
	 *
	 * @param address the address
	 * @throws InvalidIPAddressFormatException the invalid IP address format exception
	 */
	public SimpleInet6Address(byte[] address) throws InvalidIPAddressFormatException
	{
		super(address);
		if (address.length != 16)
		{
			throw new InvalidIPAddressFormatException("IPv6 address can't be " + address.length + " bytes long.");
		}
	}

	/**
	 * Gets the network address.
	 *
	 * @param mask the mask
	 * @return the network address
	 */
	@Override
	public SimpleInetAddress getNetworkAddress(int mask)
	{
		throw new NotImplementedException();
	}

	/**
	 * Gets the host address.
	 *
	 * @param mask the mask
	 * @return the host address
	 */
	@Override
	public SimpleInetAddress getHostAddress(int mask)
	{
		throw new NotImplementedException();
	}

	/**
	 * Address addition.
	 *
	 * @param increment the increment
	 * @return the simple inet address
	 */
	@Override
	public SimpleInetAddress addressAddition(long increment)
	{
		throw new NotImplementedException();
	}

}
