package space.dcce.commons.node_database.primitives;

public abstract class Primitive
{
	static public Byte getPrimitive(byte value)
	{
		return new Byte(value);
	}
	
	static public ByteArray getPrimitive(byte[] value)
	{
		return new ByteArray(value);
	}
	
	static public ByteArray getPrimitive(String value)
	{
		return new ByteArray(value.getBytes());
	}
	
	static public Integer getPrimitive(int value)
	{
		return new Integer(value);
	}
	
	static public Integer getPrimitive(long value)
	{
		return new Integer(value);
	}
	
	static public Float getPrimitive(float value)
	{
		return new Float(value);
	}
	
	static public Float getPrimitive(double value)
	{
		return new Float(value);
	}
	
	static public Boolean getPrimitive(boolean value)
	{
		return new Boolean(value);
	}
	
	@Override
	public abstract String toString();
	
	
	abstract public Object getValue();
}
