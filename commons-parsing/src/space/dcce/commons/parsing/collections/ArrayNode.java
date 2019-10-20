package space.dcce.commons.parsing.collections;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import space.dcce.commons.parsing.Node;
import space.dcce.commons.parsing.NodeType;


public class ArrayNode<E extends Node> extends ContainerNode implements List<E>
{

	private final List<E> array;


	public ArrayNode(int initialCapacity)
	{
		super(NodeType.ARRAY);
		array = new ArrayList<>(initialCapacity);
	}


	public ArrayNode()
	{
		this(1);
	}


	@Override
	public int size()
	{
		return array.size();
	}


	@Override
	public boolean isEmpty()
	{
		return array.isEmpty();
	}


	@Override
	public boolean contains(Object o)
	{
		return array.contains(o);
	}


	@Override
	public Iterator<E> iterator()
	{
		return array.iterator();
	}


	@Override
	public Object[] toArray()
	{
		return array.toArray();
	}


	@Override
	public <T> T[] toArray(T[] a)
	{
		return array.toArray(a);
	}


	@Override
	public boolean add(E e)
	{
		return array.add(e);
	}


	@Override
	public boolean remove(Object o)
	{
		return array.remove(o);
	}


	@Override
	public boolean containsAll(Collection<?> c)
	{
		return array.containsAll(c);
	}


	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		return array.addAll(c);
	}


	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		return array.addAll(index, c);
	}


	@Override
	public boolean removeAll(Collection<?> c)
	{
		return array.removeAll(c);
	}


	@Override
	public boolean retainAll(Collection<?> c)
	{
		return array.retainAll(c);
	}


	@Override
	public void clear()
	{
		array.clear();
	}


	@Override
	public E get(int index)
	{
		return array.get(index);
	}


	@Override
	public E set(int index, E element)
	{
		return array.set(index, element);
	}


	@Override
	public void add(int index, E element)
	{
		array.add(index, element);
	}


	@Override
	public E remove(int index)
	{
		return array.remove(index);
	}


	@Override
	public int indexOf(Object o)
	{
		return array.indexOf(o);
	}


	@Override
	public int lastIndexOf(Object o)
	{
		return array.lastIndexOf(o);
	}


	@Override
	public ListIterator<E> listIterator()
	{
		return array.listIterator();
	}


	@Override
	public ListIterator<E> listIterator(int index)
	{
		return array.listIterator(index);
	}


	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return array.subList(fromIndex, toIndex);
	}


	@Override
	public void addValue(Node value)
	{
		add((E) value);
	}

}
