package space.dcce.commons.data_model.primitives;

import space.dcce.commons.data_model.Node;
import space.dcce.commons.data_model.NodeType;

public class BytePrimitive extends PrimitiveNode 
{
	private Byte value;

	public BytePrimitive(String name, Byte value)
	{
		super(name, NodeType.BYTE);
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
	public byte[] encode()
	{
		return new byte[] { value };
	}


	@Override
	public Node[] getChildren()
	{
		return null;
	}


	@Override
	public String getInfoText()
	{
		return String.format("\\x%02h", value);
	}

}
