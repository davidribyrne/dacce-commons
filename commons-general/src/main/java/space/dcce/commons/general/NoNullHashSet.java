/**
 * 
 */
package space.dcce.commons.general;

import java.util.*;


/**
 * @author dbyrne
 *
 */
public class NoNullHashSet<E> extends HashSet<E>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public NoNullHashSet()
	{
		super();
	}


	public NoNullHashSet(E[] c)
	{
		this();
		addAll(c);
	}

	public NoNullHashSet(Collection<? extends E> c)
	{
		this();
		addAll(c);
	}


	public NoNullHashSet(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}


	public NoNullHashSet(int initialCapacity)
	{
		super(initialCapacity);
	}


	@Override
	public boolean add(E e)
	{
		if (e == null)
		{
			return false;
		}
		return super.add(e);
	}

	public boolean addAll(E[] c)
	{
		return addAll(Arrays.asList(c));
	}


	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		boolean modified = false;
		for (E element : c)
		{
			if (add(element))
			{
				modified = true;
			}
		}
		return modified;
	}


}
