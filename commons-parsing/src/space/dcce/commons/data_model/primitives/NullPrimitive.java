package space.dcce.commons.data_model.primitives;

import space.dcce.commons.data_model.Node;
import space.dcce.commons.data_model.NodeType;
import space.dcce.commons.general.NotImplementedException;

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
