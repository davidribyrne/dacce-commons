package space.dcce.commons.node_database.primitives;

// TODO: Auto-generated Javadoc
/**
 * The Class Primitive.
 */
public abstract class Primitive
{
	
	/**
	 * Gets the primitive.
	 *
	 * @param value the value
	 * @return the primitive
	 */
	static public Byte getPrimitive(byte value)
	{
		return new Byte(value);
	}
	
	/**
	 * Gets the primitive.
	 *
	 * @param value the value
	 * @return the primitive
	 */
	static public ByteArray getPrimitive(byte[] value)
	{
		return new ByteArray(value);
	}
	
	/**
	 * Gets the primitive.
	 *
	 * @param value the value
	 * @return the primitive
	 */
	static public ByteArray getPrimitive(String value)
	{
		return new ByteArray(value.getBytes());
	}
	
	/**
	 * Gets the primitive.
	 *
	 * @param value the value
	 * @return the primitive
	 */
	static public Integer getPrimitive(int value)
	{
		return new Integer(value);
	}
	
	/**
	 * Gets the primitive.
	 *
	 * @param value the value
	 * @return the primitive
	 */
	static public Integer getPrimitive(long value)
	{
		return new Integer(value);
	}
	
	/**
	 * Gets the primitive.
	 *
	 * @param value the value
	 * @return the primitive
	 */
	static public Float getPrimitive(float value)
	{
		return new Float(value);
	}
	
	/**
	 * Gets the primitive.
	 *
	 * @param value the value
	 * @return the primitive
	 */
	static public Float getPrimitive(double value)
	{
		return new Float(value);
	}
	
	/**
	 * Gets the primitive.
	 *
	 * @param value the value
	 * @return the primitive
	 */
	static public Boolean getPrimitive(boolean value)
	{
		return new Boolean(value);
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public abstract String toString();
	
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	abstract public Object getValue();
}
