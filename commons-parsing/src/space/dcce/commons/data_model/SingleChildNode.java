package space.dcce.commons.data_model;

public abstract class SingleChildNode extends AbstractNode
{
	private Node childNode;
	

	protected SingleChildNode(String name, Node childNode)
	{
		super(NodeType.ENCODING, name);
		this.childNode = childNode;
	}

	@Override
	public Node[] getChildren()
	{
		return new Node[] {childNode};
	}



	public Node getChildNode()
	{
		return childNode;
	}


	public void setChildNode(Node childNode)
	{
		this.childNode = childNode;
	}
	

}
