package space.dcce.commons.data_model.encoding;


import space.dcce.commons.data_model.PrintableNode;


public abstract class EncodingNode implements PrintableNode
{
	private PrintableNode childNode;


	public EncodingNode(PrintableNode childNode)
	{
		this.childNode = childNode;
	}


	public PrintableNode getChildNode()
	{
		return childNode;
	}


	public void setChildNode(PrintableNode value)
	{
		childNode = value;
	}

}
