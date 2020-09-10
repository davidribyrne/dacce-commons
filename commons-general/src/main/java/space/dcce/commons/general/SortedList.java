package space.dcce.commons.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


// TODO: Auto-generated Javadoc
/**
 * The Class SortedList.
 *
 * @author david
 * @param <E>            Comparator defaults to a case-insensitive sort on toString
 */
public class SortedList<E> extends ArrayList<E>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The comparator. */
	private Comparator<E> comparator;
	
	/** The lock. */
	private Object lock = new Object();


	/**
	 * Sets the comparator.
	 *
	 * @param comparator the new comparator
	 */
	public void setComparator(Comparator<E> comparator)
	{
		synchronized (lock)
		{
			this.comparator = comparator;
		}
	}


	/**
	 * Instantiates a new sorted list.
	 */
	public SortedList()
	{
		this((Comparator<E>) null);
	}


	/**
	 * Instantiates a new sorted list.
	 *
	 * @param c the c
	 */
	public SortedList(Collection<? extends E> c)
	{
		this(c, null);
	}


	/**
	 * Instantiates a new sorted list.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public SortedList(int initialCapacity)
	{
		this(initialCapacity, null);
	}


	/**
	 * Instantiates a new sorted list.
	 *
	 * @param comparator the comparator
	 */
	public SortedList(Comparator<E> comparator)
	{
		super();
		init(comparator);
	}


	/**
	 * Instantiates a new sorted list.
	 *
	 * @param c the c
	 * @param comparator the comparator
	 */
	public SortedList(Collection<? extends E> c, Comparator<E> comparator)
	{
		super(c);
		init(comparator);
	}


	/**
	 * Instantiates a new sorted list.
	 *
	 * @param initialCapacity the initial capacity
	 * @param comparator the comparator
	 */
	public SortedList(int initialCapacity, Comparator<E> comparator)
	{
		super(initialCapacity);
		init(comparator);
	}


	/**
	 * Inits the.
	 *
	 * @param comparator the comparator
	 */
	private void init(@SuppressWarnings("hiding") Comparator<E> comparator)
	{
		if (comparator != null)
		{
			this.comparator = comparator;
			return;
		}

		this.comparator = new Comparator<E>()
		{

			@Override
			public int compare(E o1, E o2)
			{
				String s1;
				String s2;
				if (o1 == null)
				{
					s1 = "";
				}
				else
				{
					s1 = o1.toString();
					if (s1 == null)
						s1 = "";
				}

				if (o2 == null)
				{
					s2 = "";
				}
				else
				{
					s2 = o2.toString();
					if (s2 == null)
						s2 = "";
				}


				return s1.compareTo(s2);
			}
		};
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
		synchronized (lock)
		{
			boolean result = super.add(e);
			Collections.sort(this, comparator);
			return result;
		}
	}


	/**
	 * Index is effectively ignored since the list will be re-sorted.
	 *
	 * @param index the index
	 * @param element the element
	 */
	@Override
	public void add(int index, E element)
	{
		synchronized (lock)
		{
			super.add(index, element);
			Collections.sort(this, comparator);
		}
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
		synchronized (lock)
		{
			boolean result = super.addAll(c);
			Collections.sort(this, comparator);
			return result;
		}
	}


	/**
	 * Index is effectively ignored since the list will be re-sorted.
	 *
	 * @param index the index
	 * @param c the c
	 * @return true, if successful
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		synchronized (lock)
		{
			boolean result = super.addAll(index, c);
			Collections.sort(this, comparator);
			return result;
		}
	}


}
