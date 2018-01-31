package space.dcce.commons.data_model.primatives;


import space.dcce.commons.data_model.Value;

import java.util.*;

public class Integer implements Primative
{
	private Long value;
	
	public Integer()
	{
	}
	
	public Integer(long value)
	{
		this.value = value;
	}

	@Override
	public java.lang.String asString()
	{
		return "" + value;
	}

}
