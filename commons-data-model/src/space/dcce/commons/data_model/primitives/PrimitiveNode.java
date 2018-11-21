package space.dcce.commons.data_model.primitives;

import space.dcce.commons.data_model.AbstractNode;
import space.dcce.commons.data_model.NodeType;
import space.dcce.commons.data_model.PrintableNode;


public abstract class PrimitiveNode extends AbstractNode 
{

	protected PrimitiveNode(NodeType nodeType)
	{
		super(nodeType);
	}
}
