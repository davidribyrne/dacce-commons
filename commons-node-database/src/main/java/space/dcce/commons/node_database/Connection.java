package space.dcce.commons.node_database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import space.dcce.commons.general.NoNullHashSet;
import space.dcce.commons.general.UniqueList;


public class Connection extends UniqueDatum
{
	private final UniqueList<UUID> connectionNodes;


	/**
	 * Saves to the database
	 * @param nodes
	 */
	Connection(NodeDatabase database, Node... nodes)
	{
		this(database, null, nodes);
		database.storage.addConnection(getID(), nodes);
	}


	/**
	 * Use only from KnowledgeBaseStorage and internally. 
	 * It does NOT save to the database.  
	 * 
	 * @param uuid
	 */
	Connection(NodeDatabase database, UUID uuid, Node... nodes)
	{
		super(database, uuid);
		this.connectionNodes = new UniqueList<UUID>(false);
		for (Node node: nodes)
		{
			addNode(node);
		}
		database.connectionCache.addItem(this);
	}


	/**
	 * 
	 * @param nodes
	 * @return true if all the connection nodes exactly match the nodes passed
	 */
	public boolean nodesMatch(Node... nodes)
	{
		return this.connectionNodes.equals(new NoNullHashSet<Node>(nodes));
	}


	/**
	 * 
	 * @param nodes
	 * @return true if all of the passed nodes are part of the connection. Other nodes may be in the connection too.
	 */
	public boolean containsNodes(Node... nodes)
	{
		NoNullHashSet<UUID> temp = new NoNullHashSet<UUID>(nodes.length);
		for (Node node: nodes)
		{
			temp.add(node.getID());
		}
		return this.connectionNodes.containsAll(temp);
	}

	/**
	 * 
	 * @param nodes
	 * @return true if any of the passed nodes are part of the connection. Other nodes may be in the connection too.
	 */
	public boolean containsAnyNode(Node... nodes)
	{
		for (Node node: nodes)
		{
			 if (connectionNodes.contains(node.getID()))
				 return true;
		}
		return false;
	}

	/**
	 * 
	 * @param nodes
	 * @return true if there are any connected nodes with the p.
	 */
	public boolean containsAnyNode(NodeType... types)
	{
		for (Node node: getNodes())
		{
			for (NodeType type: types)
			{
				if (node.getNodeType().equals(type))
				{
					return true;
				}
			}
		}
		return false;
	}

	public Iterable<Node> getNodes()
	{
		List<Node> n = new ArrayList<Node>(connectionNodes.size());
		for (UUID id: connectionNodes)
		{
			n.add(database.getNode(id));
		}
		return Collections.unmodifiableList(n);
	}


	/**
	 * 
	 * @return read-only list of nodes
	 */
	public Iterable<UUID> getNodeIDs()
	{
		return Collections.unmodifiableList(connectionNodes);
	}


	synchronized public void addNode(Node node)
	{
		connectionNodes.add(node.getID());
		node.addConnection(this);
		database.storage.updateConnection(getID(), node);
	}

	void restoreNode(UUID id)
	{
		connectionNodes.add(id);
	}


	public void addNodes(Node ...nodes)
	{
		for(Node node: nodes)
		{
			addNode(node);
		}
	}


	@Override
	public boolean equals(Object obj)
	{
		Connection c = (Connection) obj;
		return connectionNodes.equals(c.connectionNodes);
	}


	@Override
	public int hashCode()
	{
		return connectionNodes.hashCode();
	}

}
