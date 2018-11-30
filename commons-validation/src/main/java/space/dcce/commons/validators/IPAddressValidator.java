package space.dcce.commons.validators;


import space.dcce.commons.general.NotImplementedException;
import space.dcce.commons.netaddr.IPUtils;
import space.dcce.commons.netaddr.InvalidIPAddressFormatException;

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
			if (!IPUtils.validateAddressBlock(value))
			{
				throw new ValidationException("'" + value + "' is not a valid IP address or address block.");
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
