package space.dcce.commons.data_model.primitives;

public class NullPrimitive extends PrimitiveNode
{

	public NullPrimitive()
	{
	}


	@Override
	public byte[] getBytes()
	{
		return new byte[] {};
	}

}
