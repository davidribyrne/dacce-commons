package space.dcce.commons.node_database.primitives;

public class ByteArray extends Primitive
{
	private byte[] value;

	public byte[] getValue()
	{
		return value;
	}

	public ByteArray(byte[] value)
	{
		super();
		this.value = value;
	}

	public void setValue(byte[] value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return new String(value);
	}
}

