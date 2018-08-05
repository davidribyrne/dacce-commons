package space.dcce.commons.data_model.collections;


import space.dcce.commons.data_model.Node;
import space.dcce.commons.data_model.NodeType;
import space.dcce.commons.data_model.primitives.StringPrimitive;


public class ObjectNode extends MapNode<StringPrimitive, Node>
{
	private String clazz;


	public ObjectNode()
	{
		this(1);
	}


	public ObjectNode(int initialCapacity)
	{
		super(NodeType.OBJECT, initialCapacity);
	}


	
	public String getClazz()
	{
		return clazz;
	}


	public void setClazz(String clazz)
	{
		this.clazz = clazz;
	}

}
