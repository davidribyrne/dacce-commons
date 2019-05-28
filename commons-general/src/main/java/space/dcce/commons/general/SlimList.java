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

	@Override
	public void forEach(Consumer<? super E> action)
	{
		getList().forEach(action);
	}

	@Override
	public int size()
	{
		return getList().size();
	}

	@Override
	public boolean isEmpty()
	{
		return getList().isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return getList().contains(o);
	}

	@Override
	public Iterator<E> iterator()
	{
		return getList().iterator();
	}

	@Override
	public Object[] toArray()
	{
		return getList().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return getList().toArray(a);
	}

	@Override
	public boolean add(E e)
	{
		return getList().add(e);
	}

	@Override
	public boolean remove(Object o)
	{
		return getList().remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return getList().containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		return getList().addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		return getList().addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return getList().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return getList().retainAll(c);
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator)
	{
		getList().replaceAll(operator);
	}

	@Override
	public boolean removeIf(Predicate<? super E> filter)
	{
		return getList().removeIf(filter);
	}

	@Override
	public void sort(Comparator<? super E> c)
	{
		getList().sort(c);
	}

	@Override
	public void clear()
	{
		getList().clear();
	}

	@Override
	public boolean equals(Object o)
	{
		return getList().equals(o);
	}

	@Override
	public int hashCode()
	{
		return getList().hashCode();
	}

	@Override
	public E get(int index)
	{
		return getList().get(index);
	}

	@Override
	public E set(int index, E element)
	{
		return getList().set(index, element);
	}

	@Override
	public void add(int index, E element)
	{
		getList().add(index, element);
	}

	@Override
	public Stream<E> stream()
	{
		return getList().stream();
	}

	@Override
	public E remove(int index)
	{
		return getList().remove(index);
	}

	@Override
	public Stream<E> parallelStream()
	{
		return getList().parallelStream();
	}

	@Override
	public int indexOf(Object o)
	{
		return getList().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return getList().lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator()
	{
		return getList().listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index)
	{
		return getList().listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return getList().subList(fromIndex, toIndex);
	}

	@Override
	public Spliterator<E> spliterator()
	{
		return getList().spliterator();
	}
	
}
