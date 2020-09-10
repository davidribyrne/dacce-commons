package space.dcce.commons.general;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

// TODO: Auto-generated Javadoc
/**
 * Minimizes memory usage. Array only allocated if list has elements.
 *
 * @author dbyrne
 * @param <E> the element type
 */
public class SlimList<E> implements List<E>
{

	/** The list. */
	private List<E> list;
	
	/**
	 * Instantiates a new slim list.
	 */
	public SlimList()
	{
		
	}
	
	/**
	 * Gets the list.
	 *
	 * @return the list
	 */
	private List<E> getList()
	{
		if (list == null)
		{
			list = new ArrayList<E>(1);
		}
		return list;
	}

	/**
	 * For each.
	 *
	 * @param action the action
	 */
	@Override
	public void forEach(Consumer<? super E> action)
	{
		getList().forEach(action);
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	@Override
	public int size()
	{
		return getList().size();
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	@Override
	public boolean isEmpty()
	{
		return getList().isEmpty();
	}

	/**
	 * Contains.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	@Override
	public boolean contains(Object o)
	{
		return getList().contains(o);
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 */
	@Override
	public Iterator<E> iterator()
	{
		return getList().iterator();
	}

	/**
	 * To array.
	 *
	 * @return the object[]
	 */
	@Override
	public Object[] toArray()
	{
		return getList().toArray();
	}

	/**
	 * To array.
	 *
	 * @param <T> the generic type
	 * @param a the a
	 * @return the t[]
	 */
	@Override
	public <T> T[] toArray(T[] a)
	{
		return getList().toArray(a);
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
		return getList().add(e);
	}

	/**
	 * Removes the.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	@Override
	public boolean remove(Object o)
	{
		return getList().remove(o);
	}

	/**
	 * Contains all.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return getList().containsAll(c);
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
		return getList().addAll(c);
	}

	/**
	 * Adds the all.
	 *
	 * @param index the index
	 * @param c the c
	 * @return true, if successful
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		return getList().addAll(index, c);
	}

	/**
	 * Removes the all.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		return getList().removeAll(c);
	}

	/**
	 * Retain all.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		return getList().retainAll(c);
	}

	/**
	 * Replace all.
	 *
	 * @param operator the operator
	 */
	@Override
	public void replaceAll(UnaryOperator<E> operator)
	{
		getList().replaceAll(operator);
	}

	/**
	 * Removes the if.
	 *
	 * @param filter the filter
	 * @return true, if successful
	 */
	@Override
	public boolean removeIf(Predicate<? super E> filter)
	{
		return getList().removeIf(filter);
	}

	/**
	 * Sort.
	 *
	 * @param c the c
	 */
	@Override
	public void sort(Comparator<? super E> c)
	{
		getList().sort(c);
	}

	/**
	 * Clear.
	 */
	@Override
	public void clear()
	{
		getList().clear();
	}

	/**
	 * Equals.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object o)
	{
		return getList().equals(o);
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		return getList().hashCode();
	}

	/**
	 * Gets the.
	 *
	 * @param index the index
	 * @return the e
	 */
	@Override
	public E get(int index)
	{
		return getList().get(index);
	}

	/**
	 * Sets the.
	 *
	 * @param index the index
	 * @param element the element
	 * @return the e
	 */
	@Override
	public E set(int index, E element)
	{
		return getList().set(index, element);
	}

	/**
	 * Adds the.
	 *
	 * @param index the index
	 * @param element the element
	 */
	@Override
	public void add(int index, E element)
	{
		getList().add(index, element);
	}

	/**
	 * Stream.
	 *
	 * @return the stream
	 */
	@Override
	public Stream<E> stream()
	{
		return getList().stream();
	}

	/**
	 * Removes the.
	 *
	 * @param index the index
	 * @return the e
	 */
	@Override
	public E remove(int index)
	{
		return getList().remove(index);
	}

	/**
	 * Parallel stream.
	 *
	 * @return the stream
	 */
	@Override
	public Stream<E> parallelStream()
	{
		return getList().parallelStream();
	}

	/**
	 * Index of.
	 *
	 * @param o the o
	 * @return the int
	 */
	@Override
	public int indexOf(Object o)
	{
		return getList().indexOf(o);
	}

	/**
	 * Last index of.
	 *
	 * @param o the o
	 * @return the int
	 */
	@Override
	public int lastIndexOf(Object o)
	{
		return getList().lastIndexOf(o);
	}

	/**
	 * List iterator.
	 *
	 * @return the list iterator
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return getList().listIterator();
	}

	/**
	 * List iterator.
	 *
	 * @param index the index
	 * @return the list iterator
	 */
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return getList().listIterator(index);
	}

	/**
	 * Sub list.
	 *
	 * @param fromIndex the from index
	 * @param toIndex the to index
	 * @return the list
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return getList().subList(fromIndex, toIndex);
	}

	/**
	 * Spliterator.
	 *
	 * @return the spliterator
	 */
	@Override
	public Spliterator<E> spliterator()
	{
		return getList().spliterator();
	}
	
}
