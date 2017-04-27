package net.dacce.commons.cli;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class OptionGroup extends AbstractGroup
{

	private final List<OptionContainer> children;


	public OptionGroup(String name, String description)
	{
		super(name, description);
		children = new ArrayList<OptionContainer>();
	}


	public List<OptionContainer> getChildren()
	{
		return children;
	}


	public void addChild(OptionContainer option)
	{
		if (option != null)
		{
			children.add(option);
		}
	}
	
	@Override
	public String toString()
	{
		ToStringBuilder tsb = new ToStringBuilder(this).appendSuper(super.toString());
		for(OptionContainer child: children)
		{
			tsb.append("child", child.getName());
		}
		return tsb.build();
	}
	
}
