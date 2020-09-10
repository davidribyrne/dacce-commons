package space.dcce.commons.node_database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import space.dcce.commons.general.NotImplementedException;
import space.dcce.commons.general.UnexpectedException;


// TODO: Auto-generated Javadoc
/**
 * The Class NodeDatabase.
 */
public class NodeDatabase
{


	/** The storage. */
	public final NodeDatabaseStorage storage;

	/** The connection cache. */
	public final UniqueDatumCache<Connection> connectionCache = new UniqueDatumCache<Connection>();
	
	/** The node cache. */
	public final UniqueDatumCache<Node> nodeCache = new UniqueDatumCache<Node>();
	
	/** The node types. */
	private final Map<UUID, NodeType> nodeTypes = new HashMap<UUID, NodeType>(5);

	/** The node creation listener. */
	private NodeCreationListener nodeCreationListener;
	
	/** The connection creation listener. */
	private ConnectionCreationListener connectionCreationListener;

	/**
	 * Instantiates a new node database.
	 */
	public NodeDatabase()
	{
		storage = new NodeDatabaseStorage(this);
	}


	/**
	 * Initialize storage.
	 *
	 * @param path the path
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void initializeStorage(String path) throws SQLException, IOException
	{
		storage.loadOrCreate(path);
	}


	/**
	 * Creates the node if possible.
	 *
	 * @param nodeType the node type
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean createNodeIfPossible(NodeType nodeType, String value)
	{
		synchronized (this)
		{
			if (nodeExists(nodeType, value))
				return false;
			Node node = new Node(this, nodeType, value);
			if (nodeCreationListener != null)
			{
				nodeCreationListener.nodeCreated(node);
			}
		}
		return true;
	}


	/**
	 * Gets the connection.
	 *
	 * @param uuid the uuid
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
	 * Gets the node.
	 *
	 * @param uuid the uuid
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
	 * Requires complete match of all nodes.
	 *
	 * @param nodes the nodes
	 * @return the or create connection
	 */
	public Connection getOrCreateConnection(Node... nodes)
	{
		return getOrCreateConnection(false, nodes);
	}


	/**
	 * Gets the or create connection.
	 *
	 * @param acceptPartial            Return a connection that contains all of the specified nodes, but may contain other nodes too
	 * @param nodes the nodes
	 * @return the or create connection
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


	/**
	 * Gets the or create node.
	 *
	 * @param nodeType the node type
	 * @param value the value
	 * @return the or create node
	 */
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


	/**
	 * Gets the node.
	 *
	 * @param nodeType the node type
	 * @param value the value
	 * @return the node
	 */
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


	/**
	 * Store new node type.
	 *
	 * @param nodeType the node type
	 */
	public void storeNewNodeType(NodeType nodeType)
	{
		synchronized (this)
		{
			storage.addNodeType(nodeType);
		}
	}


	/**
	 * Node exists.
	 *
	 * @param nodeType the node type
	 * @param value the value
	 * @return true, if successful
	 */
	private boolean nodeExists(NodeType nodeType, String value)
	{
		UUID id = Node.constructUUID(nodeType, value);
		return (nodeCache.containsKey(id) || storage.nodeExists(id));
	}



	/**
	 * Gets the by name.
	 *
	 * @param name the name
	 * @param description the description
	 * @return the by name
	 */
	public synchronized NodeType getByName(String name, String description)
	{
		UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
		if (nodeTypes.containsKey(uuid))
		{
			return nodeTypes.get(uuid);
		}

		return new NodeType(this, uuid, name, description);
	}

	/**
	 * Gets the by ID.
	 *
	 * @param id the id
	 * @return the by ID
	 */
	public synchronized NodeType getByID(UUID id)
	{
		return nodeTypes.get(id);
	}

	/**
	 * Gets the node types.
	 *
	 * @return the node types
	 */
	public Map<UUID, NodeType> getNodeTypes()
	{
		return nodeTypes;
	}


	/**
	 * Gets the node creation listener.
	 *
	 * @return the node creation listener
	 */
	public NodeCreationListener getNodeCreationListener()
	{
		return nodeCreationListener;
	}


	/**
	 * Sets the node creation listener.
	 *
	 * @param nodeCreationListener the new node creation listener
	 */
	public void setNodeCreationListener(NodeCreationListener nodeCreationListener)
	{
		this.nodeCreationListener = nodeCreationListener;
	}


	/**
	 * Gets the connection creation listener.
	 *
	 * @return the connection creation listener
	 */
	public ConnectionCreationListener getConnectionCreationListener()
	{
		return connectionCreationListener;
	}


	/**
	 * Sets the connection creation listener.
	 *
	 * @param connectionCreationListener the new connection creation listener
	 */
	public void setConnectionCreationListener(ConnectionCreationListener connectionCreationListener)
	{
		this.connectionCreationListener = connectionCreationListener;
	}



}
