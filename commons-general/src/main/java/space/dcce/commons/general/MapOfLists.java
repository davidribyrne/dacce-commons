package space.dcce.commons.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO: Auto-generated Javadoc
/**
 * The Class MapOfLists.
 *
 * @param <Key> the generic type
 * @param <Value> the generic type
 */
public class MapOfLists<Key, Value>
{
	
	/** The Constant logger. */
	final static Logger logger = LoggerFactory.getLogger(MapOfLists.class);
	
	/** The map. */
	private final Map<Key, List<Value>> map;


	/**
	 * Instantiates a new map of lists.
	 */
	public MapOfLists()
	{
		map = new HashMap<Key, List<Value>>();
	}

	/**
	 * Instantiates a new map of lists.
	 *
	 * @param initialMapSize the initial map size
	 */
	public MapOfLists(int initialMapSize)
	{
		map = new HashMap<Key, List<Value>>(initialMapSize);
	}

	/**
	 * Seed keys.
	 *
	 * @param keys the keys
	 */
	public void seedKeys(Key[] keys)
	{
		for (Key key: keys)
		{
			get(key);
		}
	}

	/**
	 * Gets the.
	 *
	 * @param key the key
	 * @return the list
	 */
	public List<Value> get(Key key)
	{
		List<Value> list;
		if (map.containsKey(key))
		{
			list = map.get(key);
		}
		else
		{
			list = new ArrayList<Value>();
			map.put(key, list);
		}
		return map.get(key);
	}


	/**
	 * Put.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void put(Key key, Value value)
	{
		get(key).add(value);
	}


	/**
	 * Put all.
	 *
	 * @param key the key
	 * @param values the values
	 */
	public void putAll(Key key, Iterable<Value> values)
	{
		List<Value> list;
		if (map.containsKey(key))
		{
			list = map.get(key);
		}
		else
		{
			list = new ArrayList<Value>();
			map.put(key, list);
		}
		for (Value value : values)
		{
			list.add(value);
		}
	}

	/**
	 * Unmodifiable map of lists.
	 *
	 * @return the map
	 */
	public Map<Key, List<Value>> unmodifiableMapOfLists()
	{
		Map<Key, List<Value>> tmpMap = new HashMap<Key, List<Value>>(map.size());
		for (Key key: map.keySet())
		{
			tmpMap.put(key, Collections.unmodifiableList(map.get(key)));
		}
		return Collections.unmodifiableMap(tmpMap);
	}

	/**
	 * Key set.
	 *
	 * @return the sets the
	 */
	public Set<Key> keySet()
	{
		return Collections.unmodifiableSet(map.keySet());
	}

	/**
	 * Values.
	 *
	 * @return the collection
	 */
	public Collection<List<Value>> values()
	{
		return Collections.unmodifiableCollection(map.values());
	}

}
