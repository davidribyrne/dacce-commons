package space.dcce.commons.data_model.collections;

import java.util.HashMap;
import java.util.Map;

import space.dcce.commons.data_model.Node;


public class ObjectNode extends ContainerNode
{
	private String clazz;
	private final Map<String, Node> fields;


	public ObjectNode()
	{
		this(1);
	}


	public ObjectNode(int initialCapacity)
	{
		fields = new HashMap<>(initialCapacity);
	}


	public String getClazz()
	{
		return clazz;
	}


	public void setClazz(String clazz)
	{
		this.clazz = clazz;
	}


	public Map<String, Node> getFields()
	{
		return fields;
	}


	public Node addField(String name, Node value)
	{
		return fields.put(name, value);
	}


	public Node removeField(String name)
	{
		return fields.remove(name);
	}


	public void clearFields()
	{
		fields.clear();
	}
}
