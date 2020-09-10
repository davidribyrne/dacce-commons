package space.dcce.commons.node_database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import space.dcce.commons.general.UniqueList;

// TODO: Auto-generated Javadoc
/**
 * The Class Node.
 */
public class Node extends UniqueDatum
{
	
	/** The connections. */
	private final UniqueList<UUID> connections;
	
	/** The node type. */
	private final NodeType nodeType;
	
	/** The value. */
	private final String value;



	/**
	 * Instantiates a new node.
	 *
	 * @param database the database
	 * @param nodeType the node type
	 * @param value the value
	 */
	Node(NodeDatabase database, NodeType nodeType, String value)
	{
		this(database, null, nodeType, value);
		database.storage.addNode(this);
	}
	
	
	/**
	 * Only use from KnowledgeBaseStorage.
	 *
	 * @param database the database
	 * @param uuid the uuid
	 * @param nodeType the node type
	 * @param value Cannot be null
	 * @throws NullPointerException the null pointer exception
	 */
	Node(NodeDatabase database, UUID uuid, NodeType nodeType, String value) throws NullPointerException
	{
		super(database, uuid == null ? constructUUID(nodeType, value): uuid);
		if (value == null)
			throw new NullPointerException("Node value cannot be null");
		this.value = value;
		this.nodeType = nodeType;
		this.connections = new UniqueList<UUID>(false);
		database.nodeCache.addItem(this);
	}

	/**
	 * Construct UUID.
	 *
	 * @param nodeType the node type
	 * @param value the value
	 * @return the uuid
	 */
	public static UUID constructUUID(NodeType nodeType, String value)
	{
		return UUID.nameUUIDFromBytes((nodeType.getID().toString() + value).getBytes());
	}

	/**
	 * Gets the connection UUI ds.
	 *
	 * @return the connection UUI ds
	 */
	public Iterable<UUID> getConnectionUUIDs()
	{
		return Collections.unmodifiableList(connections);
	}
	
	/**
	 * Gets the connections.
	 *
	 * @return the connections
	 */
	public Iterable<Connection> getConnections()
	{
		List<Connection> c = new ArrayList<Connection>(connections.size());
		for (UUID u: connections)
		{
			c.add(database.getConnection(u));
		}
		
		return Collections.unmodifiableList(c);
	}


	/**
	 * Adds the connection.
	 *
	 * @param connection the connection
	 */
	public void addConnection(Connection connection)
	{
		if (connection == null)
		{
			throw new NullPointerException("Connection cannot be null");
		}
		connections.add(connection.getID());
	}

	/**
	 * Checks for connection to.
	 *
	 * @param nodes the nodes
	 * @return The first connection found that has this node and one of the passed nodes. Null if none are found.
	 */
	public Connection hasConnectionTo(Node... nodes)
	{
		for (Connection connection: getConnections())
		{
			if (connection.containsAnyNode(nodes))
			{
				return connection;
			}
		}
		return null;
	}


	/**
	 * Checks for connection to types.
	 *
	 * @param types the types
	 * @return The first connection found that has this node and one of the passed nodes. Null if none are found.
	 */
	public Connection hasConnectionToTypes(NodeType... types)
	{
		for (Connection connection: getConnections())
		{
			if (connection.containsAnyNode(types))
			{
				return connection;
			}
		}
		return null;
	}

//	public Timestamp getCreationTime()
//	{
//		return creationTime;
//	}


	/**
 * Gets the value.
 *
 * @return the value
 */
public String getValue()
	{
		return value;
	}


	/**
	 * Gets the node type.
	 *
	 * @return the node type
	 */
	public NodeType getNodeType()
	{
		return nodeType;
	}
}
