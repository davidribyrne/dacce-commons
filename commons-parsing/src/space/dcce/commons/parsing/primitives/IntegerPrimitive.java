package space.dcce.commons.parsing.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import space.dcce.commons.parsing.Node;
import space.dcce.commons.parsing.NodeType;

public class IntegerPrimitive extends PrimitiveNode
{
	private Long value;
	private Size size;
	private boolean signed;
	private ByteOrder order;

	
	public IntegerPrimitive(String name, Long value)
	{
		this(name, value, Size.INT64, true, ByteOrder.LITTLE_ENDIAN);
	}

	public IntegerPrimitive(String name, Long value, Size size, boolean signed, ByteOrder order)
	{
		super(name, NodeType.INTEGER);
		this.size = size;
		this.signed = signed;
		this.order = order;
		this.value = value;
	}

	
	/**
	 * Automatically sets the size to INT8
	 * @param value
	 */
	public void setValue(Byte value)
	{
		this.value = (long) value;
		size = Size.INT8;
	}

	
	/**
	 * Automatically sets the size to INT16
	 * @param value
	 */
	public void setValue(short value)
	{
		this.value = (long) value;
		size = Size.INT16;
	}


	/**
	 * Automatically sets the size to INT32
	 * @param value
	 */
	public void setValue(int value)
	{
		this.value = (long) value;
		size = Size.INT32;
	}
	

	/**
	 * Automatically sets the size to INT64
	 * @param value
	 */
	public void setValue(long value)
	{
		this.value = (long) value;
		size = Size.INT64;
	}

	
	public enum Size
	{
		INT8, INT16, INT32, INT64;
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


	public Size getSize()
	{
		return size;
	}


	public void setSize(Size size)
	{
		this.size = size;
	}


	public boolean isSigned()
	{
		return signed;
	}


	public void setSigned(boolean signed)
	{
		this.signed = signed;
	}

	@Override
	public Node[] getChildren()
	{
		return null;
	}

	@Override
	public byte[] encode()
	{
		switch(size)
		{
			case INT8:
				return ByteBuffer.allocate(1).order(order).put(value.byteValue()).array();
				
			case INT16:
				return ByteBuffer.allocate(2).order(order).putShort(value.shortValue()).array();
				
			case INT32:
				return ByteBuffer.allocate(4).order(order).putInt(value.intValue()).array();
				
			case INT64:
				return ByteBuffer.allocate(8).order(order).putLong(value.longValue()).array();
		}
		
		throw new IllegalStateException("Unknown int length");
	}

	@Override
	public String getInfoText()
	{
		// TODO Auto-generated method stub
		return null;
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
