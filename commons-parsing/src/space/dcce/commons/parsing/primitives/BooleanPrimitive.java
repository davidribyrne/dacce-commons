package space.dcce.commons.parsing.primitives;

import space.dcce.commons.parsing.Node;
import space.dcce.commons.parsing.NodeType;

public class BooleanPrimitive extends PrimitiveNode
{
	private Boolean value;


	public BooleanPrimitive(String name, boolean value)
	{
		super(name, NodeType.BOOLEAN);
		this.value = value;
	}


	
	public Boolean getValue()
	{
		return value;
	}


	public void setValue(Boolean value)
	{
		this.value = value;
	}


	@Override
	public Node[] getChildren()
	{
		return null;
	}



}
