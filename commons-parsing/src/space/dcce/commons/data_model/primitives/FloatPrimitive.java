package space.dcce.commons.data_model.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import space.dcce.commons.data_model.Node;
import space.dcce.commons.data_model.NodeType;


public class FloatPrimitive extends PrimitiveNode
{
	private Double value;
	private ByteOrder order;
	private Size size;


	public FloatPrimitive(String name, Double value)
	{
		this(name, value, ByteOrder.LITTLE_ENDIAN, Size.FLOAT64);
	}
	
	public FloatPrimitive(String name, Double value, ByteOrder order, Size size)
	{
		super(name, NodeType.FLOAT);
		this.value = value;
		this.order = order;
		this.size = size;
	}

	public enum Size
	{
		FLOAT32, FLOAT64;
	}

	@Override
	public byte[] encode()
	{
		switch(size)
		{
			case FLOAT32:
			{
				int i = Float.floatToIntBits(value.floatValue());
				return ByteBuffer.allocate(4).order(order).putInt(i).array();
			}
			case FLOAT64:
			{
				long l = Double.doubleToLongBits(value);
				return ByteBuffer.allocate(4).order(order).putLong(l).array();
			}
		}
		throw new IllegalStateException("Unknown float size");
	}


	/**
	 * @return the value
	 */
	public Double getValue()
	{
		return value;
	}


	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Double value)
	{
		this.value = value;
	}


	@Override
	public Node[] getChildren()
	{
		return null;
	}


	@Override
	public String getInfoText()
	{
		return String.valueOf(value);
	}


	public ByteOrder getOrder()
	{
		return order;
	}


	public void setOrder(ByteOrder order)
	{
		this.order = order;
	}

}
