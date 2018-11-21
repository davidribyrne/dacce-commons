package space.dcce.commons.data_model;

public abstract class SingleChildNode extends AbstractNode
{
	private PrintableNode childNode;

	protected SingleChildNode(PrintableNode childNode)
	{
		super(NodeType.ENCODING);
		this.childNode = childNode;
	}


	public PrintableNode getChildNode()
	{
		return childNode;
	}


	public void setChildNode(PrintableNode childNode)
	{
		this.childNode = childNode;
	}
	

}
