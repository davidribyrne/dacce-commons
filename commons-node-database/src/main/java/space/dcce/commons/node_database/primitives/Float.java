package space.dcce.commons.node_database.primitives;

// TODO: Auto-generated Javadoc
/**
 * The Class Float.
 */
public class Float extends Primitive
{
	
	/** The value. */
	private double value;

	/**
	 * Instantiates a new float.
	 *
	 * @param value the value
	 */
	public Float(double value)
	{
		super();
		this.value = value;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Double getValue()
	{
		return new Double(value);
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(double value)
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
