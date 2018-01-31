package space.dcce.commons.data_model.primatives;


import space.dcce.commons.data_model.Value;

import java.util.*;

public class String implements Primative
{
	private java.lang.String value;
	public String()
	{
	}
	
	public String(java.lang.String value)
	{
		this.value = value;
	}
	
	public void setValue(java.lang.String value)
	{
		this.value = value;
	}

	@Override
	public java.lang.String asString()
	{
		return value;
	}

}
