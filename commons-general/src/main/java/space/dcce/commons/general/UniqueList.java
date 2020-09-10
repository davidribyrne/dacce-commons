package space.dcce.commons.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;



// TODO: Auto-generated Javadoc
/**
 * The Class UniqueList.
 *
 * @param <T> the generic type
 */
public class UniqueList<T> extends ArrayList<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The allow nulls. */
	private boolean allowNulls = false;

	/**
	 * Instantiates a new unique list.
	 *
	 * @param allowNulls the allow nulls
	 * @param initialCapacity the initial capacity
	 */
	public UniqueList(boolean allowNulls, int initialCapacity)
	{
		super(initialCapacity);
		this.allowNulls = allowNulls;
	}

	/**
	 * Instantiates a new unique list.
	 *
	 * @param allowNulls the allow nulls
	 */
	public UniqueList(boolean allowNulls)
	{
		super();
		this.allowNulls = allowNulls;
	}

	/**
	 * Instantiates a new unique list.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public UniqueList(int initialCapacity)
	{
		super(initialCapacity);
	}


//	public UniqueList(Collection<? extends T> paramCollection)
//	{
//		super(paramCollection);
//	}



	/**
 * Sets the.
 *
 * @param paramInt the param int
 * @param paramE the param E
 * @return the t
 */
@Override
	public T set(int paramInt, T paramE)
	{
		if (paramE == null && !allowNulls)
			return null;
		return super.set(paramInt, paramE);
	}


	/**
	 * Adds the.
	 *
	 * @param paramE the param E
	 * @return true, if successful
	 */
	@Override
	public boolean add(T paramE)
	{
		if (paramE == null && !allowNulls)
			return false;
		
		if (contains(paramE))
		{
			return false;
		}
		return super.add(paramE);
	}


	/**
	 * Adds the.
	 *
	 * @param paramInt the param int
	 * @param paramE the param E
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.ArrayList#add(int, java.lang.Object)
	 * Since this is a unique list, this will fail if the item is already in the list
	 */
	@Override
	public void add(int paramInt, T paramE)
	{
		if (paramE == null && !allowNulls)
			return;
		
		if (!contains(paramE))
		{
			super.add(paramInt, paramE);
		}
	}


	/**
	 * Adds the all.
	 *
	 * @param items the items
	 * @return true, if successful
	 */
	public boolean addAll(T[] items)
	{
		boolean changed = false;
		for (T item : items)
		{
			changed = add(item) || changed;
		}
		return changed;
	}


	/**
	 * Adds the all.
	 *
	 * @param paramCollection the param collection
	 * @return true, if successful
	 */
	@Override
	public boolean addAll(Collection<? extends T> paramCollection)
	{
		boolean changed = false;
		for (T item : paramCollection)
		{
			changed = add(item) || changed;
		}
		return changed;
	}


	/**
	 * Adds the all.
	 *
	 * @param paramInt the param int
	 * @param paramCollection the param collection
	 * @return true, if successful
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#addAll(int, java.util.Collection)
	 * Only unique items will be added
	 */
	@Override
	public boolean addAll(int paramInt, Collection<? extends T> paramCollection)
	{
		if ((paramInt > size()) || (paramInt < 0))
		{
			throw new IndexOutOfBoundsException("Index: " + paramInt + ", Size: " + size());
		}
		boolean changed = false;
		for (T item : paramCollection)
		{
			if (!contains(item))
			{
				add(paramInt++, item);
				changed = true;
			}
		}

		return changed;
	}


	/**
	 * Replace all.
	 *
	 * @param paramUnaryOperator the param unary operator
	 */
	@Override
	public void replaceAll(UnaryOperator<T> paramUnaryOperator)
	{
		throw new NotImplementedException();
	}

	
}
