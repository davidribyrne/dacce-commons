package net.dacce.commons.general;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


// TODO: Make this thread safe
public class IndexedCache<ValueType> implements Iterable<ValueType>
{
	/** Each key has only one value */
	transient private final ConcurrentMap<Field, SingleValueIndex> singleValueIndexes;
	
	/** Each key can have multiple values */
	transient private final ConcurrentMap<Field, MultiValueIndex> multiValueIndexes;
	private final UniqueList<ValueType> indexedObjects;


	public IndexedCache()
	{
		singleValueIndexes = new ConcurrentHashMap<Field, SingleValueIndex>(1);
		multiValueIndexes = new ConcurrentHashMap<Field, MultiValueIndex>(1);
		indexedObjects = new UniqueList<ValueType>();
	}

	private class SingleValueIndex extends ConcurrentHashMap<Object, ValueType>
	{
		private static final long serialVersionUID = 1L;
	}

	private class MultiValueIndex extends ConcurrentHashMap<Object, List<ValueType>>
	{
		private static final long serialVersionUID = 1L;

		void addItem(Object key, ValueType value)
		{
			List<ValueType> list = get(key);
			if (list == null)
			{
				list = new UniqueList<ValueType>(1);
				put(key, list);
			}
			list.add(value);
		}
	}


	public void addAll(Iterable<ValueType> objects)
	{
		for (ValueType object : objects)
		{
			add(object);
		}
	}

	public void add(ValueType object)
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



	public boolean containsKey(Field field, Object key)
	{
		return getMember(field, key) != null;
	}

	public boolean containsValue(Object value)
	{
		return indexedObjects.contains(value);
	}

	private void generateSingleValueIndex(SingleValueIndex index, Field field) throws IllegalArgumentException, IllegalAccessException
	{
		for (ValueType object : getAllMembers())
		{
			Object key = field.get(object);
			if (key instanceof Collection)
			{
				Collection<?> keys = (Collection<?>) key;
				for(Object k: keys)
				{
					index.put(k, object);
				}
			}
			else
			{
				index.put(key, object);
			}
		}
	}



	private void generateMultivalueIndex(MultiValueIndex index, Field field) throws IllegalArgumentException,
			IllegalAccessException
	{
		// index = <address, List<Hostname>>
		for (ValueType object : getAllMembers()) // object = hostname
		{
			Object k = field.get(object); // key = address
			Collection<Object> keys;
			if (k instanceof Collection)
			{
				keys = (Collection<Object>) k;
			}
			else
			{
				keys = Collections.singletonList(k);
			}
			
			for (Object key: keys)
			{
				List<ValueType> objects = index.get(key); // objects = List<Hostname> for address
				if (objects == null) // There isn't a list for this address yet
				{
					objects = new UniqueList<ValueType>();
					index.put(key, objects); // Put address list in index
				}
				objects.add(object); // put hostname in address list
			}
		}
	}


	public List<ValueType> getMembers(Field field, Object key)
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

	public ValueType getMember(Field field, Object key)
	{
		return (ValueType) getSingleValueIndex(field).get(key);
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
				generateSingleValueIndex(index, field);
			}
			catch (SecurityException | IllegalArgumentException | IllegalAccessException e)
			{
				throw new UnexpectedException("This shouldn't have happened: " + e.getLocalizedMessage(), e);
			}
		}
		return index;
	}
	
	
	public List<ValueType> getAllMembers()
	{
		return indexedObjects;
	}


	@Override
	public Iterator<ValueType> iterator()
	{
		return indexedObjects.iterator();
	}

	@Override
	public String toString()
	{
		return CollectionUtils.joinObjects("\n", this);
	}
		
}
