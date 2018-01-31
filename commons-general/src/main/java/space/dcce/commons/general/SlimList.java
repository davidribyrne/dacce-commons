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

/**
 * Minimizes memory usage. Array only allocated if list has elements.
 * @author dbyrne
 *
 */
public class SlimList<E> implements List<E>
{

	private List<E> list;
	
	public SlimList()
	{
		
	}
	
	private List<E> getList()
	{
		if (list == null)
		{
			list = new ArrayList<E>(1);
		}
		return list;
	}

	public void forEach(Consumer<? super E> action)
	{
		getList().forEach(action);
	}

	public int size()
	{
		return getList().size();
	}

	public boolean isEmpty()
	{
		return getList().isEmpty();
	}

	public boolean contains(Object o)
	{
		return getList().contains(o);
	}

	public Iterator<E> iterator()
	{
		return getList().iterator();
	}

	public Object[] toArray()
	{
		return getList().toArray();
	}

	public <T> T[] toArray(T[] a)
	{
		return getList().toArray(a);
	}

	public boolean add(E e)
	{
		return getList().add(e);
	}

	public boolean remove(Object o)
	{
		return getList().remove(o);
	}

	public boolean containsAll(Collection<?> c)
	{
		return getList().containsAll(c);
	}

	public boolean addAll(Collection<? extends E> c)
	{
		return getList().addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c)
	{
		return getList().addAll(index, c);
	}

	public boolean removeAll(Collection<?> c)
	{
		return getList().removeAll(c);
	}

	public boolean retainAll(Collection<?> c)
	{
		return getList().retainAll(c);
	}

	public void replaceAll(UnaryOperator<E> operator)
	{
		getList().replaceAll(operator);
	}

	public boolean removeIf(Predicate<? super E> filter)
	{
		return getList().removeIf(filter);
	}

	public void sort(Comparator<? super E> c)
	{
		getList().sort(c);
	}

	public void clear()
	{
		getList().clear();
	}

	public boolean equals(Object o)
	{
		return getList().equals(o);
	}

	public int hashCode()
	{
		return getList().hashCode();
	}

	public E get(int index)
	{
		return getList().get(index);
	}

	public E set(int index, E element)
	{
		return getList().set(index, element);
	}

	public void add(int index, E element)
	{
		getList().add(index, element);
	}

	public Stream<E> stream()
	{
		return getList().stream();
	}

	public E remove(int index)
	{
		return getList().remove(index);
	}

	public Stream<E> parallelStream()
	{
		return getList().parallelStream();
	}

	public int indexOf(Object o)
	{
		return getList().indexOf(o);
	}

	public int lastIndexOf(Object o)
	{
		return getList().lastIndexOf(o);
	}

	public ListIterator<E> listIterator()
	{
		return getList().listIterator();
	}

	public ListIterator<E> listIterator(int index)
	{
		return getList().listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex)
	{
		return getList().subList(fromIndex, toIndex);
	}

	public Spliterator<E> spliterator()
	{
		return getList().spliterator();
	}
	
}
