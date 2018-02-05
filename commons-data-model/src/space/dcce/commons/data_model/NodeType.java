package space.dcce.commons.data_model;


public enum NodeType
{
	MAP("Map"), ARRAY("Array"),

	STRING("String"), BYTE("Byte"), CHARACTER("Character"), INTEGER("Integer"), FLOAT("Float"), BOOLEAN("Boolean"), NULL("Null"),

	ENCODING("Encoding");


	private final String name;


	private NodeType(String name)
	{
		this.name = name;
	}


	public String getName()
	{
		return name;
	}


}
