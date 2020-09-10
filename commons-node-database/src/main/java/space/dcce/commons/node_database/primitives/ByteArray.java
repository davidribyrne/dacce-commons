package space.dcce.commons.node_database.primitives;

// TODO: Auto-generated Javadoc
/**
 * The Class ByteArray.
 */
public class ByteArray extends Primitive
{
	
	/** The value. */
	private byte[] value;

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public byte[] getValue()
	{
		return value;
	}

	/**
	 * Instantiates a new byte array.
	 *
	 * @param value the value
	 */
	public ByteArray(byte[] value)
	{
		super();
		this.value = value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(byte[] value)
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
		return new String(value);
	}
}

