package space.dcce.commons.node_database;

import java.util.UUID;


public class NodeType extends UniqueDatum
{

	protected String name;
	protected String description;
	private Object lock = new Object();

	
	NodeType(NodeDatabase database, UUID uuid, String name, String description)
	{
		this(database, uuid, name, description, false);
	}

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


	public String getName()
	{
		return name;
	}


	public String getDescription()
	{
		return description;
	}

}
