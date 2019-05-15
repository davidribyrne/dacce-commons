package space.dcce.commons.node_database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import space.dcce.commons.general.UniqueList;

public class Node extends UniqueDatum
{
	private final UniqueList<UUID> connections;
	private final NodeType nodeType;
	private final String value;



	Node(NodeDatabase database, NodeType nodeType, String value)
	{
		this(database, null, nodeType, value);
		database.storage.addNode(this);
	}
	
	
	/**
	 * Only use from KnowledgeBaseStorage
	 * @param uuid
	 * @param value Cannot be null
	 * @param type Cannot be null
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

	public static UUID constructUUID(NodeType nodeType, String value)
	{
		return UUID.nameUUIDFromBytes((nodeType.getID().toString() + value).getBytes());
	}

	public Iterable<UUID> getConnectionUUIDs()
	{
		return Collections.unmodifiableList(connections);
	}
	
	public Iterable<Connection> getConnections()
	{
		List<Connection> c = new ArrayList<Connection>(connections.size());
		for (UUID u: connections)
		{
			c.add(database.getConnection(u));
		}
		
		return Collections.unmodifiableList(c);
	}


	public void addConnection(Connection connection)
	{
		if (connection == null)
		{
			throw new NullPointerException("Connection cannot be null");
		}
		connections.add(connection.getID());
	}

	/**
	 * 
	 * @param node
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
	 * 
	 * @param node
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


	public String getValue()
	{
		return value;
	}


	public NodeType getNodeType()
	{
		return nodeType;
	}
}
