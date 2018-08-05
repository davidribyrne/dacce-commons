package space.dcce.commons.data_model.primitives;

import space.dcce.commons.data_model.NodeType;
import space.dcce.commons.data_model.PrintableNode;

public class FloatPrimitive extends PrimitiveNode implements PrintableNode
{
	private Double value;

	public FloatPrimitive()
	{
		super(NodeType.FLOAT);
	}
	
	public FloatPrimitive(Double value)
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
	public Double getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Double value)
	{
		this.value = value;
	}

}
