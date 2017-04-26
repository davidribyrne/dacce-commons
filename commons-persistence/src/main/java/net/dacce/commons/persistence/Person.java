package net.dacce.commons.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class Person
{
	private final static Logger logger = LoggerFactory.getLogger(Person.class);
	private String name;
	private int age;
	private int id;

	public Person()
	{
		
	}


	public Person(String name, int age, int id)
	{
		super();
		this.name = name;
		this.age = age;
		this.id = id;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public int getAge()
	{
		return age;
	}


	public void setAge(int age)
	{
		this.age = age;
	}


	public int getId()
	{
		return id;
	}


	public void setId(int id)
	{
		this.id = id;
	}
}
