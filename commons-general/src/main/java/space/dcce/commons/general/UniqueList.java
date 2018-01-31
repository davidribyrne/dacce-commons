package space.dcce.commons.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;


public class UniqueList<T> extends ArrayList<T>
{

	private static final long serialVersionUID = 1L;
	private boolean allowNulls;

	public UniqueList(boolean allowNulls)
	{
		this.allowNulls = allowNulls;
	}


	public UniqueList(Collection<? extends T> paramCollection)
	{
		super(paramCollection);
	}


	public UniqueList(int paramInt)
	{
		super(paramInt);
	}


	@Override
	public T set(int paramInt, T paramE)
	{
		if (paramE == null && !allowNulls)
			return null;
		return super.set(paramInt, paramE);
	}


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


	@Override
	public void replaceAll(UnaryOperator<T> paramUnaryOperator)
	{
		throw new NotImplementedException();
	}


}
