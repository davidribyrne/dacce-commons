package space.dcce.commons.node_database.primitives;

// TODO: Auto-generated Javadoc
/**
 * The Class Boolean.
 */
public class Boolean extends Primitive
{
	
	/** The value. */
	private boolean value;

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Boolean getValue()
	{
		return new Boolean(value);
	}

	/**
	 * Instantiates a new boolean.
	 *
	 * @param value the value
	 */
	public Boolean(boolean value)
	{
		super();
		this.value = value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(boolean value)
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
