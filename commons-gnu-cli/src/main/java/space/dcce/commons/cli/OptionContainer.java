package space.dcce.commons.cli;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class OptionContainer
{
	private final String name;
	protected RootOptions root;
	
	protected OptionContainer(String name, RootOptions root)
	{
		this.name = name;
		this.root = root;
	}

	public String getName()
	{
		return name;
	}
	
	
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("name", name).build();
	}

	protected RootOptions getRoot()
	{
		return root;
	}

}
