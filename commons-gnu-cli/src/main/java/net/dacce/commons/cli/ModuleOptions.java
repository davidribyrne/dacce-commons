package net.dacce.commons.cli;

import net.dacce.commons.cli.exceptions.ParseException;
import net.dacce.commons.general.StringUtils;

/**
 * This option is a string that will be parsed into another option set
 * @author david
 *
 */
public class ModuleOptions extends Option
{
	private final static String MODULE_OPTION_PREFIX = "mod_";
	private final Options subOptions;
	
	public ModuleOptions(String longOption, String description, String moduleName) throws IllegalArgumentException
	{
		super("", MODULE_OPTION_PREFIX + longOption, moduleName + ": " + description, true, true, "", "\"Quoted sub-argument string\"");
		if (StringUtils.isEmptyOrNull(longOption))
		{
			throw new IllegalArgumentException("longOption cannot be empty");
		}
		subOptions = new Options();
		setMultipleCalls(false);
	}

	
	@Override
	public void addValue(String value) throws ParseException
	{
		super.addValue(value);
		String args[] = CommandLine.translateCommandline(value);
		GnuParser.parse(subOptions, args, false);
	}


	public Options getSubOptions()
	{
		return subOptions;
	}


}
