package space.dcce.commons.data_model.primitives;

public class BytePrimitive extends PrimitiveNode
{
	private Byte value;


	public BytePrimitive(Byte value)
	{
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
