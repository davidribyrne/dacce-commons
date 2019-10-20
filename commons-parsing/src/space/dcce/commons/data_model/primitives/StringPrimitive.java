package space.dcce.commons.data_model.primitives;

import space.dcce.commons.data_model.Node;
import space.dcce.commons.data_model.NodeType;

public class StringPrimitive extends PrimitiveNode
{
	private String value;


	public StringPrimitive(String name, String value)
	{
		super(name, NodeType.STRING);
		this.value = value;
	}



	public void setValue(String value)
	{
		this.value = value;
	}




	public String getValue()
	{
		return value;
	}


	@Override
	public Node[] getChildren()
	{
		return null;
	}


	@Override
	public String getInfoText()
	{
		return value;
	}
	
}
