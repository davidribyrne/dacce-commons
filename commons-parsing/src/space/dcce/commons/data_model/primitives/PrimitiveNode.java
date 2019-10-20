package space.dcce.commons.data_model.primitives;

import space.dcce.commons.data_model.AbstractNode;
import space.dcce.commons.data_model.NodeType;


public abstract class PrimitiveNode extends AbstractNode 
{

	protected PrimitiveNode(String name, NodeType nodeType)
	{
		super(name, nodeType);
	}
}
