package space.dcce.commons.data_model.primitives;

import space.dcce.commons.data_model.NodeType;
import space.dcce.commons.data_model.PrintableNode;

public class BytePrimitive extends PrimitiveNode implements PrintableNode
{
	private Byte value;

	public BytePrimitive()
	{
		super(NodeType.BYTE);
	}

	public BytePrimitive(Byte value)
	{
		this();
		this.value = value;
	}


	public Byte getValue()
	{
		return value;
	}


	public void setValue(Byte value)
	{
		this.value = value;
	}


	@Override
	public byte[] getBytes()
	{
		return new byte[] { value };
	}


}
