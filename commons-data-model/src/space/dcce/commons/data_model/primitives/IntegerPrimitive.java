package space.dcce.commons.data_model.primitives;

public class IntegerPrimitive extends PrimitiveNode
{
	private Long value;


	public IntegerPrimitive()
	{
	}


	public IntegerPrimitive(long value)
	{
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
