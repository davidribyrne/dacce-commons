package net.dacce.commons.general;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


// TODO: Make this thread safe
public class IndexedCache<T> implements Iterable<T>
{
	/** Each key has only one value */
	private final ConcurrentMap<Field, SingleValueIndex> singleValueIndexes;
	
	/** Each key can have multiple values */
	private final ConcurrentMap<Field, MultiValueIndex> multiValueIndexes;
	private final UniqueList<T> indexedObjects;


	public IndexedCache()
	{
		singleValueIndexes = new ConcurrentHashMap<Field, SingleValueIndex>(1);
		multiValueIndexes = new ConcurrentHashMap<Field, MultiValueIndex>(1);
		indexedObjects = new UniqueList<T>();
	}

	private class SingleValueIndex extends ConcurrentHashMap<Object, Object>
	{
		private static final long serialVersionUID = 1L;
	}

	private class MultiValueIndex extends ConcurrentHashMap<Object, List<Object>>
	{
		private static final long serialVersionUID = 1L;

		void addItem(Object key, Object value)
		{
			List<Object> list = get(key);
			if (list == null)
			{
				list = new UniqueList<Object>(1);
				put(key, list);
			}
			list.add(value);
		}
	}


	public void addAll(Iterable<T> objects)
	{
		for (T object : objects)
		{
			add(object);
		}
	}


	public void add(T object)
	{
		if (indexedObjects.add(object))
		{
			for (Field field : multiValueIndexes.keySet())
			{
				try
				{
					multiValueIndexes.get(field).addItem(field.get(object), object);
				}
				catch (IllegalArgumentException | IllegalAccessException e)
				{
					throw new UnexpectedException("This shouldn't have happened: " + e.getLocalizedMessage(), e);
				}
			}

			for (Field field : singleValueIndexes.keySet())
			{
				try
				{
					singleValueIndexes.get(field).put(field.get(object), object);
				}
				catch (IllegalArgumentException | IllegalAccessException e)
				{
					throw new UnexpectedException("This shouldn't have happened: " + e.getLocalizedMessage(), e);
				}
			}
		}
	}


	public Map<?, ?> getIndex(Field field)
	{
		return singleValueIndexes.get(field);
	}


	public boolean contains(Field field, Object key)
	{
		return getMember(field, key) != null;
	}


	private void generateIndex(SingleValueIndex index, Field field) throws IllegalArgumentException, IllegalAccessException
	{
		for (Object object : getObjects())
		{
			index.put(field.get(object), object);
		}
	}


//	private Field getFieldByName(String fieldName)
//	{
//		try
//		{
//			return indexedObjects.get(0).getClass().getField(fieldName);
//		}
//		catch (NoSuchFieldException | SecurityException e)
//		{
//			throw new UnexpectedException("This shouldn't have happened: " + e.getLocalizedMessage(), e);
//		}
//	}


	private void generateMultivalueIndex(ConcurrentMap<Object, List<Object>> index, Field field) throws IllegalArgumentException,
			IllegalAccessException
	{
		// index = <address, List<Hostname>>
		for (Object object : getObjects()) // object = hostname
		{
			Object key = field.get(object); // key = address
			List<Object> objects = index.get(key); // objects = List<Hostname> for address
			if (objects == null) // There isn't a list for this address yet
			{
				objects = new UniqueList<Object>();
				index.put(key, objects); // Put address list in index
			}
			objects.add(object); // put hostname in address list
		}
	}


	public List<Object> getMembers(Field field, Object key)
	{
		// field = address, key = hostname object, field(key) = address
		return getMultiValueIndex(field).get(key);
	}

	private MultiValueIndex getMultiValueIndex(Field field)
	{
		field.setAccessible(true);
		MultiValueIndex index = multiValueIndexes.get(field.getName()); // index = <address, List<Hostname>>
		if (index == null)
		{
			index = new MultiValueIndex();
			multiValueIndexes.put(field, index);
			try
			{
				generateMultivalueIndex(index, field);
			}
			catch (SecurityException | IllegalArgumentException | IllegalAccessException e)
			{
				throw new UnexpectedException("This shouldn't have happened: " + e.getLocalizedMessage(), e);
			}
		}
		return index;
	}

	public Object getMember(Field field, Object key)
	{
		return getSingleValueIndex(field).get(key);
	}


	private SingleValueIndex getSingleValueIndex(Field field)
	{
		field.setAccessible(true);
		SingleValueIndex index = singleValueIndexes.get(field);
		if (index == null)
		{
			index = new SingleValueIndex();
			singleValueIndexes.put(field, index);
			try
			{
				generateIndex(index, field);
			}
			catch (SecurityException | IllegalArgumentException | IllegalAccessException e)
			{
				throw new UnexpectedException("This shouldn't have happened: " + e.getLocalizedMessage(), e);
			}
		}
		return index;
	}
	
	
	private List<T> getObjects()
	{
		return indexedObjects;
	}


	@Override
	public Iterator<T> iterator()
	{
		return indexedObjects.iterator();
	}

	@Override
	public String toString()
	{
		return CollectionUtils.joinObjects("\n", this);
	}
		
}
