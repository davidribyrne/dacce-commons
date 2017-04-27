package net.dacce.commons.cli;

public class ModuleOption extends Option
{


	public ModuleOption(String longOption, String description) throws IllegalArgumentException
	{
		super(null, longOption, description);
	}


	public ModuleOption(String longOption, String description, boolean argAccepted, boolean argRequired, String defaultValue,
			String argDescription) throws IllegalArgumentException
	{
		super(null, longOption, description, argAccepted, argRequired, defaultValue, argDescription);
	}
}
