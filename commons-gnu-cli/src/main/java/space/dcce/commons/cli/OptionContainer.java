package space.dcce.commons.cli;

import org.apache.commons.lang3.builder.ToStringBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionContainer.
 */
public abstract class OptionContainer
{
	
	/** The name. */
	private final String name;
	
	/** The root. */
	protected RootOptions root;
	
	/**
	 * Instantiates a new option container.
	 *
	 * @param name the name
	 * @param root the root
	 */
	protected OptionContainer(String name, RootOptions root)
	{
		this.name = name;
		this.root = root;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("name", name).build();
	}

	/**
	 * Gets the root.
	 *
	 * @return the root
	 */
	protected RootOptions getRoot()
	{
		return root;
	}

}
