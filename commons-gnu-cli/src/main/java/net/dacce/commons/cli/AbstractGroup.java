package net.dacce.commons.cli;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractGroup implements OptionContainer
{
	private String name;
	private String description;


	public AbstractGroup(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("name", name).append("description", description).build();
	}

	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}
}
