package space.dcce.commons.parsing.collections;


import space.dcce.commons.parsing.Node;
import space.dcce.commons.parsing.NodeType;


public class KeyValuePair<K extends Node, V extends Node> extends ContainerNode
{
	private K key;


	private V value;

	public KeyValuePair()
	{
		super(NodeType.NAME_VALUE_PAIR);
	}
	
	public KeyValuePair(K name)
	{
		this();
		this.key = name;
	}
	
	public KeyValuePair(K name, V value)
	{
		this(name);
		this.value = value;
		
	}
	


	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}

	public K getKey()
	{
		return key;
	}

	public void setKey(K key)
	{
		this.key = key;
	}

	public V getValue()
	{
		return value;
	}

	public void setValue(V value)
	{
		this.value = value;
	}

	@Override
	public void addValue(Node value)
	{
		setValue((V)value);
	}

}
