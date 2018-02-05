package space.dcce.commons.data_model.primitives;

public class FloatPrimitive extends PrimitiveNode
{
	private Double value;

	public FloatPrimitive()
	{
	}
	
	public FloatPrimitive(Double value)
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
