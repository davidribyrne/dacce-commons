package space.dcce.commons.data_model.primatives;

import java.util.*;

public class BooleanValue implements Primative
{
	private java.lang.Boolean value;
	
	public BooleanValue()
	{
	}

	public BooleanValue(boolean value)
	{
		this.value = value;
	}
	

	@Override
	public java.lang.String asString()
	{
		return "" + value;
	}

}
