package space.dcce.commons.validators;


import space.dcce.commons.general.NotImplementedException;
import space.dcce.commons.netaddr.IP4Utils;
import space.dcce.commons.netaddr.IPUtils;
import space.dcce.commons.netaddr.InvalidIPAddressFormatException;

// TODO: Auto-generated Javadoc
/**
 * The Class IPAddressValidator.
 */
public class IPAddressValidator implements Validator
{

	/** The allow blocks. */
	private boolean allowBlocks;
	
	/**
	 * Instantiates a new IP address validator.
	 *
	 * @param allowIPv6 the allow I pv 6
	 * @param allowBlocks the allow blocks
	 */
	public IPAddressValidator(boolean allowIPv6, boolean allowBlocks)
	{
		this.allowBlocks = allowBlocks;
		if (allowIPv6)
		{
			throw new NotImplementedException("IPv6 is not implemented yet.");
		}
	}

	/**
	 * Validate.
	 *
	 * @param value the value
	 * @throws ValidationException the validation exception
	 */
	@Override
	public void validate(String value) throws ValidationException
	{
		if (allowBlocks)
		{
			if (!IP4Utils.validateAddressBlock(value))
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
