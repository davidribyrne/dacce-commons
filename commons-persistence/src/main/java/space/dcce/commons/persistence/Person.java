package space.dcce.commons.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Person.
 */
public class Person
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(Person.class);
	
	/** The name. */
	private String name;
	
	/** The age. */
	private int age;
	
	/** The id. */
	private int id;

	/**
	 * Instantiates a new person.
	 */
	public Person()
	{
		
	}


	/**
	 * Instantiates a new person.
	 *
	 * @param name the name
	 * @param age the age
	 * @param id the id
	 */
	public Person(String name, int age, int id)
	{
		super();
		this.name = name;
		this.age = age;
		this.id = id;
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
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * Gets the age.
	 *
	 * @return the age
	 */
	public int getAge()
	{
		return age;
	}


	/**
	 * Sets the age.
	 *
	 * @param age the new age
	 */
	public void setAge(int age)
	{
		this.age = age;
	}


	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}


	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id)
	{
		this.id = id;
	}
}
