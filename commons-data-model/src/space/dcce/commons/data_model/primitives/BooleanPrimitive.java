package space.dcce.commons.data_model.primitives;

public class BooleanPrimitive extends PrimitiveNode
{
	private Boolean value;
	private Mode mode = Mode.TRUE_FALSE;

	public BooleanPrimitive()
	{
	}


	public BooleanPrimitive(boolean value)
	{
		this.value = value;
	}

	public BooleanPrimitive(String value)
	{
		value = value.toLowerCase();
		if (value.equals("true"))
		{
			this.value = true;
		}
		else if (value.equals("false"))
		{
			this.value = false;
		}
		
		else if (value.equals("on"))
		{
			this.value = true;
			mode = Mode.ON_OFF;
		}
		else if (value.equals("off"))
		{
			this.value = false;
			mode = Mode.ON_OFF;
		}
		
		else if (value.equals("yes"))
		{
			this.value = true;
			mode = Mode.YES_NO;
		}
		else if (value.equals("no"))
		{
			this.value = false;
			mode = Mode.YES_NO;
		}
		
		else if (value.equals("enabled"))
		{
			this.value = true;
			mode = Mode.ENABLED_DISABLED;
		}
		else if (value.equals("disabled"))
		{
			this.value = false;
			mode = Mode.ENABLED_DISABLED;
		}
		
		throw new IllegalArgumentException("Value must match true|false|on|off|yes|no|enabled|disabled");
	}


	@Override
	public byte[] getBytes()
	{
		return (value + "").getBytes();
	}

	public static enum Mode
	{
		TRUE_FALSE, YES_NO, ON_OFF, ENABLED_DISABLED;
	}

	/**
	 * @return the mode
	 */
	public Mode getMode()
	{
		return mode;
	}


	/**
	 * @param mode the mode to set
	 */
	public void setMode(Mode mode)
	{
		this.mode = mode;
	}
}
