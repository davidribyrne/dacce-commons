package space.dcce.commons.node_database.primitives;

public class Boolean extends Primitive
{
	private boolean value;

	public Boolean getValue()
	{
		return new Boolean(value);
	}

	public Boolean(boolean value)
	{
		super();
		this.value = value;
	}

	public void setValue(boolean value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return value + "";
	}
	
}
