package space.dcce.commons.parsing.primitives;

import space.dcce.commons.general.NotImplementedException;
import space.dcce.commons.parsing.Node;
import space.dcce.commons.parsing.NodeType;

public class NullPrimitive extends PrimitiveNode
{

	public NullPrimitive(String name)
	{
		super(name, NodeType.NULL);
	}

	@Override
	public Node[] getChildren()
	{
		return null;
	}


	@Override
	public String getInfoText()
	{
		return "null";
	}

}
