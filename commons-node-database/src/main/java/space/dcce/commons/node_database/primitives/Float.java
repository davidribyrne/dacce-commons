package space.dcce.commons.node_database.primitives;

public class Float extends Primitive
{
	private double value;

	public Float(double value)
	{
		super();
		this.value = value;
	}

	public Double getValue()
	{
		return new Double(value);
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return value + "";
	}
	
}
