package space.dcce.commons.node_database.primitives;

// TODO: Auto-generated Javadoc
/**
 * The Class Integer.
 */
public class Integer extends Primitive
{
	
	/** The value. */
	private long value;

	/**
	 * Instantiates a new integer.
	 *
	 * @param value the value
	 */
	public Integer(long value)
	{
		super();
		this.value = value;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Long getValue()
	{
		return new Long(value);
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(long value)
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
