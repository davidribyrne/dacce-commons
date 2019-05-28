package space.dcce.commons.node_database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import space.dcce.commons.general.NotImplementedException;
import space.dcce.commons.general.UnexpectedException;


public class NodeDatabase
{


	public final NodeDatabaseStorage storage;

	public final UniqueDatumCache<Connection> connectionCache = new UniqueDatumCache<Connection>();
	public final UniqueDatumCache<Node> nodeCache = new UniqueDatumCache<Node>();
	private final Map<UUID, NodeType> nodeTypes = new HashMap<UUID, NodeType>(5);

	private NodeCreationListener nodeCreationListener;
	private ConnectionCreationListener connectionCreationListener;

	public NodeDatabase()
	{
		storage = new NodeDatabaseStorage(this);
	}


	public void initializeStorage(String path) throws SQLException, IOException
	{
		storage.loadOrCreate(path);
	}


//	public boolean createNodeIfPossible(NodeType nodeType, String value)
//	{
//		synchronized (this)
//		{
//			if (nodeExists(nodeType, value))
//				return false;
//			new Node(this, nodeType, value);
//
//		}
//		return true;
//	}


	/**
	 * 
	 * @param uuid
	 * @return Null if connection not found
	 */
	public Connection getConnection(UUID uuid)
	{
		/*
		 * First, try cache, then try database. If those don't work, return null;
		 */
		Connection c = connectionCache.getValue(uuid);
		if (c != null)
			return c;
		try
		{
			return storage.getConnection(uuid);
		}
		catch (SQLException e)
		{
			throw new UnexpectedException("Database error: " + e.getMessage(), e);
		}
	}


	/**
	 * 
	 * @param uuid
	 * @return Null if node not found
	 */
	public Node getNode(UUID uuid)
	{
		/*
		 * First, try cache, then try database. If those don't work, return null;
		 */
		Node n = nodeCache.getValue(uuid);
		if (n != null)
			return n;
		try
		{
			return storage.getNode(uuid);
		}
		catch (SQLException e)
		{
			throw new UnexpectedException("Database error: " + e.getMessage(), e);
		}
	}


	/**
	 * 
	 * Requires complete match of all nodes
	 * 
	 * @param nodes
	 * @return
	 */
	public Connection getOrCreateConnection(Node... nodes)
	{
		return getOrCreateConnection(false, nodes);
	}


	/**
	 * 
	 * @param acceptPartial
	 *            Return a connection that contains all of the specified nodes, but may contain other nodes too
	 * @param nodes
	 * @return
	 */
	public Connection getOrCreateConnection(boolean acceptPartial, Node... nodes)
	{
		synchronized (this)
		{
			// First, check to see if the connection already exists

			for (Node node : nodes)
			{
				for (UUID uuid : node.getConnectionUUIDs())
				{
					Connection c = getConnection(uuid);
					if (acceptPartial)
					{
						if (c.containsNodes(nodes))
							return c;
					}
					else
					{
						if (c.nodesMatch(nodes))
							return c;
					}
				}
			}

			// No match, so create new connection

			Connection c = new Connection(this, nodes);
			for (Node node : nodes)
			{
				node.addConnection(c);
			}
			if (connectionCreationListener != null)
			{
				connectionCreationListener.connectionCreated(c);
			}
			return c;
		}
	}


	public Node getOrCreateNode(NodeType nodeType, String value)
	{
		synchronized (this)
		{
			Node node = getNode(nodeType, value);
			if (node != null)
				return node;

			node = new Node(this, nodeType, value);
			if (nodeCreationListener != null)
			{
				nodeCreationListener.nodeCreated(node);
			}
			return node;
		}
	}


	public Node getNode(NodeType nodeType, String value)
	{
		UUID id = Node.constructUUID(nodeType, value);
		/*
		 * First, try cache, then try database. If those don't work, it must not exist, so return null
		 */
		Node n = nodeCache.getValue(id);
		if (n != null)
			return n;
		try
		{
			return storage.getNode(id);
		}
		catch (SQLException e)
		{
			throw new UnexpectedException("Database error: " + e.getMessage(), e);
		}
	}


	public void storeNewNodeType(NodeType nodeType)
	{
		synchronized (this)
		{
			storage.addNodeType(nodeType);
		}
	}


//	private boolean nodeExists(NodeType nodeType, String value)
//	{
//		UUID id = Node.constructUUID(nodeType, value);
//		return (nodeCache.containsKey(id) || storage.nodeExists(id));
//	}



	public synchronized NodeType getByName(String name, String description)
	{
		UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
		if (nodeTypes.containsKey(uuid))
		{
			return nodeTypes.get(uuid);
		}

		return new NodeType(this, uuid, name, description);
	}

	public synchronized NodeType getByID(UUID id)
	{
		return nodeTypes.get(id);
	}

	public Map<UUID, NodeType> getNodeTypes()
	{
		return nodeTypes;
	}


	public NodeCreationListener getNodeCreationListener()
	{
		return nodeCreationListener;
	}


	public void setNodeCreationListener(NodeCreationListener nodeCreationListener)
	{
		this.nodeCreationListener = nodeCreationListener;
	}


	public ConnectionCreationListener getConnectionCreationListener()
	{
		return connectionCreationListener;
	}


	public void setConnectionCreationListener(ConnectionCreationListener connectionCreationListener)
	{
		this.connectionCreationListener = connectionCreationListener;
	}



}
