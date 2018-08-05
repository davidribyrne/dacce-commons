package space.dcce.commons.data_model.primitives;

import space.dcce.commons.data_model.NodeType;
import space.dcce.commons.data_model.PrintableNode;

public class StringPrimitive extends PrimitiveNode implements PrintableNode
{
	private String value;


	public StringPrimitive()
	{
		super(NodeType.STRING);
	}


	public StringPrimitive(String value)
	{
		this();
		this.value = value;
	}


	public void setValue(String value)
	{
		this.value = value;
	}


	@Override
	public byte[] getBytes()
	{
		return value.getBytes();
	}


	public String getString()
	{
		return value;
	}
	
}
