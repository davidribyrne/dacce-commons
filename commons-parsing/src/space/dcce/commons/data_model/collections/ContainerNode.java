package space.dcce.commons.data_model.collections;


import space.dcce.commons.data_model.AbstractNode;
import space.dcce.commons.data_model.Node;
import space.dcce.commons.data_model.NodeType;


public abstract class ContainerNode extends AbstractNode
{

	protected ContainerNode(NodeType nodeType)
	{
		super(nodeType);
	}


	public abstract void addValue (Node value);
}
