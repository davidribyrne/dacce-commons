package space.dcce.commons.general;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


// TODO: Auto-generated Javadoc
/**
 * The Class IndexedCache.
 *
 * @param <ValueType> the generic type
 */
// TODO: Make this thread safe
public class IndexedCache<ValueType> extends UniqueList<ValueType>
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**  Each key has only one value. */
	transient private final ConcurrentMap<Field, SingleValueIndex> singleValueIndexes;
	
	/**  Each key can have multiple values. */
	transient private final ConcurrentMap<Field, MultiValueIndex> multiValueIndexes;


	/**
	 * Instantiates a new indexed cache.
	 */
	public IndexedCache()
	{
		super(false);
		singleValueIndexes = new ConcurrentHashMap<Field, SingleValueIndex>(1);
		multiValueIndexes = new ConcurrentHashMap<Field, MultiValueIndex>(1);
	}

	/**
	 * The Class SingleValueIndex.
	 */
	private class SingleValueIndex extends ConcurrentHashMap<Object, ValueType>
	{
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;
	}

	/**
	 * The Class MultiValueIndex.
	 */
	private class MultiValueIndex extends ConcurrentHashMap<Object, List<ValueType>>
	{
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Adds the item.
		 *
		 * @param key the key
		 * @param value the value
		 */
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


	/**
	 * Adds the all.
	 *
	 * @param objects the objects
	 */
	public void addAll(Iterable<ValueType> objects)
	{
		for (ValueType object : objects)
		{
			add(object);
		}
	}

	/**
	 * Adds the.
	 *
	 * @param object the object
	 * @return true, if successful
	 */
	@Override
	public boolean add(ValueType object)
	{
		if (super.add(object))
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
			return true;
		}
		return false;
	}



	/**
	 * Contains key.
	 *
	 * @param field the field
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean containsKey(Field field, Object key)
	{
		return getMember(field, key) != null;
	}

	/**
	 * Contains value.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean containsValue(Object value)
	{
		return super.contains(value);
	}

	/**
	 * Generate single value index.
	 *
	 * @param index the index
	 * @param field the field
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	private void generateSingleValueIndex(SingleValueIndex index, Field field) throws IllegalArgumentException, IllegalAccessException
	{
		for (ValueType object : this)
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



	/**
	 * Generate multivalue index.
	 *
	 * @param index the index
	 * @param field the field
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	private void generateMultivalueIndex(MultiValueIndex index, Field field) throws IllegalArgumentException,
			IllegalAccessException
	{
		// index = <address, List<Hostname>>
		for (ValueType object : this) // object = hostname
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
					objects = new UniqueList<ValueType>(false);
					index.put(key, objects); // Put address list in index
				}
				objects.add(object); // put hostname in address list
			}
		}
	}


	/**
	 * Gets the members.
	 *
	 * @param field the field
	 * @param key the key
	 * @return the members
	 */
	public List<ValueType> getMembers(Field field, Object key)
	{
		// field = address, key = hostname object, field(key) = address
		return getMultiValueIndex(field).get(key);
	}

	/**
	 * Gets the multi value index.
	 *
	 * @param field the field
	 * @return the multi value index
	 */
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

	/**
	 * Gets the member.
	 *
	 * @param field the field
	 * @param key the key
	 * @return the member
	 */
	public ValueType getMember(Field field, Object key)
	{
		return getSingleValueIndex(field).get(key);
	}


	/**
	 * Gets the single value index.
	 *
	 * @param field the field
	 * @return the single value index
	 */
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
	
	



	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return CollectionUtils.joinObjects("\n", this);
	}
		
}
