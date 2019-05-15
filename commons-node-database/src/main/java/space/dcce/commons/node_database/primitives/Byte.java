package space.dcce.commons.node_database.primitives;

public class Byte extends Primitive
{
	private byte value;

	public Byte(byte value)
	{
		super();
		this.value = value;
	}

	public Byte getValue()
	{
		return new Byte(value);
	}

	public void setValue(byte value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return value + "";
	}
	

}
