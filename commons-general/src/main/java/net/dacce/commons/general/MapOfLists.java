package net.dacce.commons.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


	public List<Value> get(Object key)
	{
		return map.get(key);
	}


	public void put(Key key, Value value)
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
		list.add(value);
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


}
