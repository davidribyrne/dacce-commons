package net.dacce.commons.cli;

import java.util.ArrayList;
import java.util.List;


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
		children.add(option);
	}
}
