package space.dcce.commons.parsing.collections;


import space.dcce.commons.parsing.Node;
import space.dcce.commons.parsing.NodeType;
import space.dcce.commons.parsing.primitives.StringPrimitive;


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
