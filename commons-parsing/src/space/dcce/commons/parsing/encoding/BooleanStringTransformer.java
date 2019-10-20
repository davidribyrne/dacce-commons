package space.dcce.commons.parsing.encoding;

import space.dcce.commons.parsing.*;

public class BooleanStringTransformer extends AbstractNode
{
	private Boolean value = null; 
	private BooleanMode mode;

	public BooleanStringTransformer(String name)
	{
		super(name, NodeType.TRANSFORMER);
	}
	
	public String getString()
	{
		switch(mode)
		{
			case ENABLED_DISABLED:
				return value ? "enabled" : "disabled";
			case ON_OFF:
				return value ? "on" : "off";
			case TRUE_FALSE:
				return value ? "true" : "false";
			case YES_NO:
				return value ? "yes" : "no";
		}
		throw new IllegalStateException("Unknown boolean mode");
	}


	public String setString(String value)
	{
		value = value.toLowerCase();
		if (value.equals("true"))
		{
			this.value = true;
			mode = BooleanMode.TRUE_FALSE;
		}
		else if (value.equals("false"))
		{
			this.value = false;
			mode = BooleanMode.TRUE_FALSE;
		}
		
		else if (value.equals("on"))
		{
			this.value = true;
			mode = BooleanMode.ON_OFF;
		}
		else if (value.equals("off"))
		{
			this.value = false;
			mode = BooleanMode.ON_OFF;
		}
		
		else if (value.equals("yes"))
		{
			this.value = true;
			mode = BooleanMode.YES_NO;
		}
		else if (value.equals("no"))
		{
			this.value = false;
			mode = BooleanMode.YES_NO;
		}
		
		else if (value.equals("enabled"))
		{
			this.value = true;
			mode = BooleanMode.ENABLED_DISABLED;
		}
		else if (value.equals("disabled"))
		{
			this.value = false;
			mode = BooleanMode.ENABLED_DISABLED;
		}
		
		throw new IllegalArgumentException("Value must match true|false|on|off|yes|no|enabled|disabled");
	}

	public void setBoolean(boolean value)
	{
		this.value = value;
	}
	
	public Boolean getBoolean()
	{
		return value;
	}
	

	/**
	 * @return the boolean mode
	 */
	public BooleanMode getMode()
	{
		return mode;
	}


	/**
	 * @param mode the mode to set
	 */
	public void setMode(BooleanMode mode)
	{
		this.mode = mode;
	}


	@Override
	public Node[] getChildren()
	{
		return null;
	}

	@Override
	public String getInfoText()
	{
		return getString();
	}
}
