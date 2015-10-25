package net.dacce.commons.validators;


import net.dacce.commons.general.NotImplementedException;
import net.dacce.commons.netaddr.IPUtils;
import net.dacce.commons.netaddr.InvalidIPAddressFormatException;

public class IPAddressValidator implements Validator
{

	private boolean allowBlocks;
	public IPAddressValidator(boolean allowIPv6, boolean allowBlocks)
	{
		this.allowBlocks = allowBlocks;
		if (allowIPv6)
		{
			throw new NotImplementedException("IPv6 is not implemented yet.");
		}
	}

	@Override
	public void validate(String value) throws ValidationException
	{
		if (allowBlocks)
		{
			try
			{
				IPUtils.parseAddressBlock(value);
			}
			catch (InvalidIPAddressFormatException e)
			{
				throw new ValidationException("'" + value + "' is not a valid IP address or address block.", e);
			}
		}
		else
		{
			try
			{
				IPUtils.fromString(value);
			}
			catch (InvalidIPAddressFormatException e)
			{
				throw new ValidationException("'" + value + "' is not a valid IP address.", e);
			}
		}
	}
}
