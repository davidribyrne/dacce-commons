package space.dcce.commons.parsing.primitives;

import space.dcce.commons.parsing.AbstractNode;
import space.dcce.commons.parsing.NodeType;


public abstract class PrimitiveNode extends AbstractNode 
{

	protected PrimitiveNode(String name, NodeType nodeType)
	{
		super(name, nodeType);
	}
}
