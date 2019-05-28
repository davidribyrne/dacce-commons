package space.dcce.commons.cli;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class OptionGroup extends OptionContainer
{

	private final List<OptionContainer> children;
	private String description;


	protected OptionGroup(RootOptions root, String name, String description)
	{
		super(name, root);
		this.description = description;
		children = new ArrayList<OptionContainer>();
	}

	
	public Option addOption(String shortOption, String longOption, String description)
	{
		Option opt = new Option(getRoot(), shortOption, longOption, description);
		addOption(opt);
		return opt;
	}
	
	public Option addOption(String shortOption, String longOption, String description, boolean argAccepted, boolean argRequired, String defaultValue, String argDescription)
	{
		Option opt = new Option(getRoot(), shortOption,  longOption,  description,  argAccepted,  argRequired,  defaultValue,  argDescription);
		addOption(opt);
		return opt;
	}

	protected void addOption(Option option)
	{
		children.add(option);
		getRoot().processOption(option);
	}
	
	
	public OptionGroup addOptionGroup(String name, String description)
	{
		OptionGroup group = new OptionGroup(getRoot(), name, description);
		addOptionGroup(group);
		return group;
	}
	
	protected void addOptionGroup(OptionGroup group)
	{
		children.add(group);
		getRoot().processGroup(group);
	}
	
	public List<OptionContainer> getChildren()
	{
		return children;
	}


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}
	
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
