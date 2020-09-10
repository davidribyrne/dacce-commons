/**
 * 
 */
package space.dcce.commons.general;

import java.util.*;


// TODO: Auto-generated Javadoc
/**
 * The Class NoNullHashSet.
 *
 * @author dbyrne
 * @param <E> the element type
 */
public class NoNullHashSet<E> extends HashSet<E>
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new no null hash set.
	 */
	public NoNullHashSet()
	{
		super();
	}


	/**
	 * Instantiates a new no null hash set.
	 *
	 * @param c the c
	 */
	public NoNullHashSet(E[] c)
	{
		this();
		addAll(c);
	}

	/**
	 * Instantiates a new no null hash set.
	 *
	 * @param c the c
	 */
	public NoNullHashSet(Collection<? extends E> c)
	{
		this();
		addAll(c);
	}


	/**
	 * Instantiates a new no null hash set.
	 *
	 * @param initialCapacity the initial capacity
	 * @param loadFactor the load factor
	 */
	public NoNullHashSet(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}


	/**
	 * Instantiates a new no null hash set.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public NoNullHashSet(int initialCapacity)
	{
		super(initialCapacity);
	}


	/**
	 * Adds the.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	@Override
	public boolean add(E e)
	{
		if (e == null)
		{
			return false;
		}
		return super.add(e);
	}

	/**
	 * Adds the all.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	public boolean addAll(E[] c)
	{
		return addAll(Arrays.asList(c));
	}


	/**
	 * Adds the all.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
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
