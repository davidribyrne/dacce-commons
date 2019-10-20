package space.dcce.commons.data_model;


public abstract class AbstractNode implements Node
{
	final private NodeType nodeType;
	final private String name;

	protected AbstractNode(String name, NodeType nodeType)
	{
		this.nodeType = nodeType;
		this.name = name;
	}
	

	public NodeType getNodeType()
	{
		return nodeType;
	}


	public String getName()
	{
		return name;
	}
}
