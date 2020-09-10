package space.dcce.commons.cli;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;


// TODO: Auto-generated Javadoc
/**
 * The Class OptionGroup.
 */
public class OptionGroup extends OptionContainer
{

	/** The children. */
	private final List<OptionContainer> children;
	
	/** The description. */
	private String description;


	/**
	 * Instantiates a new option group.
	 *
	 * @param root the root
	 * @param name the name
	 * @param description the description
	 */
	protected OptionGroup(RootOptions root, String name, String description)
	{
		super(name, root);
		this.description = description;
		children = new ArrayList<OptionContainer>();
	}

	
	/**
	 * Adds the option.
	 *
	 * @param shortOption the short option
	 * @param longOption the long option
	 * @param description the description
	 * @return the option
	 */
	public Option addOption(String shortOption, String longOption, String description)
	{
		Option opt = new Option(getRoot(), shortOption, longOption, description);
		addOption(opt);
		return opt;
	}
	
	/**
	 * Adds the option.
	 *
	 * @param shortOption the short option
	 * @param longOption the long option
	 * @param description the description
	 * @param argAccepted the arg accepted
	 * @param argRequired the arg required
	 * @param defaultValue the default value
	 * @param argDescription the arg description
	 * @return the option
	 */
	public Option addOption(String shortOption, String longOption, String description, boolean argAccepted, boolean argRequired, String defaultValue, String argDescription)
	{
		Option opt = new Option(getRoot(), shortOption,  longOption,  description,  argAccepted,  argRequired,  defaultValue,  argDescription);
		addOption(opt);
		return opt;
	}

	/**
	 * Adds the option.
	 *
	 * @param option the option
	 */
	protected void addOption(Option option)
	{
		children.add(option);
		getRoot().processOption(option);
	}
	
	
	/**
	 * Adds the option group.
	 *
	 * @param name the name
	 * @param description the description
	 * @return the option group
	 */
	public OptionGroup addOptionGroup(String name, String description)
	{
		OptionGroup group = new OptionGroup(getRoot(), name, description);
		addOptionGroup(group);
		return group;
	}
	
	/**
	 * Adds the option group.
	 *
	 * @param group the group
	 */
	protected void addOptionGroup(OptionGroup group)
	{
		children.add(group);
		getRoot().processGroup(group);
	}
	
	/**
	 * Gets the children.
	 *
	 * @return the children
	 */
	public List<OptionContainer> getChildren()
	{
		return children;
	}


	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}


	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		ToStringBuilder tsb = new ToStringBuilder(this).appendSuper(super.toString()).append("description", description);
		for(OptionContainer child: children)
		{
			tsb.append("child", child.getName());
		}
		return tsb.build();
	}
	
}
