package space.dcce.commons.data_model;

import java.util.*;

public enum Type
{
	MAP("Map"), ARRAY("Array"),
	
	STRING("String"), BYTE("Byte"), CHARACTER("Character"), INTEGER("Integer"), FLOAT("Float"), BOOLEAN("Boolean"),
	
	ENCODING("Encoding");
	
	
	private String name;
	private Type(String name)
	{
		this.name = name;
	}
	
	
}
