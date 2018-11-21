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


public class MapOfLists<Key, Value>
{
	final static Logger logger = LoggerFactory.getLogger(MapOfLists.class);
	private final Map<Key, List<Value>> map;


	public MapOfLists()
	{
		map = new HashMap<Key, List<Value>>();
	}

	public MapOfLists(int initialMapSize)
	{
		map = new HashMap<Key, List<Value>>(initialMapSize);
	}

	public void seedKeys(Key[] keys)
	{
		for (Key key: keys)
		{
			get(key);
		}
	}

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


	public void put(Key key, Value value)
	{
		get(key).add(value);
	}


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

	public Map<Key, List<Value>> unmodifiableMapOfLists()
	{
		Map<Key, List<Value>> tmpMap = new HashMap<Key, List<Value>>(map.size());
		for (Key key: map.keySet())
		{
			tmpMap.put(key, Collections.unmodifiableList(map.get(key)));
		}
		return Collections.unmodifiableMap(tmpMap);
	}

	public Set<Key> keySet()
	{
		return Collections.unmodifiableSet(map.keySet());
	}

	public Collection<List<Value>> values()
	{
		return Collections.unmodifiableCollection(map.values());
	}

}