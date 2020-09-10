package space.dcce.commons.node_database.primitives;

// TODO: Auto-generated Javadoc
/**
 * The Class Byte.
 */
public class Byte extends Primitive
{
	
	/** The value. */
	private byte value;

	/**
	 * Instantiates a new byte.
	 *
	 * @param value the value
	 */
	public Byte(byte value)
	{
		super();
		this.value = value;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Byte getValue()
	{
		return new Byte(value);
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(byte value)
	{
		this.value = value;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return value + "";
	}
	

}
