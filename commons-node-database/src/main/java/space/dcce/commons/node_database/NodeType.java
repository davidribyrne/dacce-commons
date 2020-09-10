package space.dcce.commons.node_database;

import java.util.UUID;


// TODO: Auto-generated Javadoc
/**
 * The Class NodeType.
 */
public class NodeType extends UniqueDatum
{

	/** The name. */
	protected String name;
	
	/** The description. */
	protected String description;
	
	/** The lock. */
	private Object lock = new Object();

	
	/**
	 * Instantiates a new node type.
	 *
	 * @param database the database
	 * @param uuid the uuid
	 * @param name the name
	 * @param description the description
	 */
	NodeType(NodeDatabase database, UUID uuid, String name, String description)
	{
		this(database, uuid, name, description, false);
	}

	/**
	 * Instantiates a new node type.
	 *
	 * @param database the database
	 * @param uuid the uuid
	 * @param name the name
	 * @param description the description
	 * @param restore the restore
	 */
	NodeType(NodeDatabase database, UUID uuid, String name, String description, boolean restore)
	{
		super(database, uuid);
		synchronized (lock)
		{
			this.name = name;
			this.description = description;
			if (database.getNodeTypes().containsKey(getID()))
			{
				throw new IllegalStateException("Type already existed in TYPES");
			}
			database.getNodeTypes().put(getID(), this);
			if (!restore)
			{
				database.storeNewNodeType(this);
			}
		}
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

}
