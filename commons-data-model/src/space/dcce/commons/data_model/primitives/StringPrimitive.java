package space.dcce.commons.data_model.primitives;

public class StringPrimitive extends PrimitiveNode
{
	private String value;


	public StringPrimitive()
	{
	}


	public StringPrimitive(String value)
	{
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


}
