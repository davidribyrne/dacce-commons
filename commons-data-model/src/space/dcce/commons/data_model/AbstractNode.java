package space.dcce.commons.data_model;


public abstract class AbstractNode implements Node
{
	final private NodeType nodeType;

	protected AbstractNode(NodeType nodeType)
	{
		this.nodeType = nodeType;
	}
	

	public NodeType getNodeType()
	{
		return nodeType;
	}
}
