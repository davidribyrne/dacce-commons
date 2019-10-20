package space.dcce.commons.parsing.collections;


import space.dcce.commons.parsing.AbstractNode;
import space.dcce.commons.parsing.Node;
import space.dcce.commons.parsing.NodeType;


public abstract class ContainerNode extends AbstractNode
{

	protected ContainerNode(NodeType nodeType)
	{
		super(nodeType);
	}


	public abstract void addValue (Node value);
}
