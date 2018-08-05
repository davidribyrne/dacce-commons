package space.dcce.commons.data_model.primitives;

import space.dcce.commons.data_model.NodeType;
import space.dcce.commons.data_model.PrintableNode;

public class IntegerPrimitive extends PrimitiveNode implements PrintableNode
{
	private Long value;


	public IntegerPrimitive()
	{
		super(NodeType.INTEGER);
	}


	public IntegerPrimitive(long value)
	{
		this();
		this.value = value;
	}


	@Override
	public byte[] getBytes()
	{
		return (value + "").getBytes();
	}


	/**
	 * @return the value
	 */
	public Long getValue()
	{
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(Long value)
	{
		this.value = value;
	}


}
