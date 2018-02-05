package space.dcce.commons.data_model.collections;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import space.dcce.commons.data_model.Node;


public class MapNode<K extends Node, V extends Node> extends ContainerNode implements Map<K, V>
{
	private Map<K, V> map;


	public MapNode()
	{
		this(1);
	}


	public MapNode(int initialCapacity)
	{
		map = new HashMap<>(initialCapacity);
	}


	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	@Override
	public int size()
	{
		return map.size();
	}


	/**
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return map.isEmpty();
	}


	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}


	/**
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value)
	{
		return map.containsValue(value);
	}


	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public V get(Object key)
	{
		return map.get(key);
	}


	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value)
	{
		return map.put(key, value);
	}


	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public V remove(Object key)
	{
		return map.remove(key);
	}


	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		map.putAll(m);
	}


	/**
	 *
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear()
	{
		map.clear();
	}


	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<K> keySet()
	{
		return map.keySet();
	}


	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<V> values()
	{
		return map.values();
	}


	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<Entry<K, V>> entrySet()
	{
		return map.entrySet();
	}


	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see java.util.Map#getOrDefault(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V getOrDefault(Object key, V defaultValue)
	{
		return map.getOrDefault(key, defaultValue);
	}


	/**
	 * @param action
	 * @see java.util.Map#forEach(java.util.function.BiConsumer)
	 */
	@Override
	public void forEach(BiConsumer<? super K, ? super V> action)
	{
		map.forEach(action);
	}


	/**
	 * @param function
	 * @see java.util.Map#replaceAll(java.util.function.BiFunction)
	 */
	@Override
	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
	{
		map.replaceAll(function);
	}


	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#putIfAbsent(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V putIfAbsent(K key, V value)
	{
		return map.putIfAbsent(key, value);
	}


	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#remove(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean remove(Object key, Object value)
	{
		return map.remove(key, value);
	}


	/**
	 * @param key
	 * @param oldValue
	 * @param newValue
	 * @return
	 * @see java.util.Map#replace(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean replace(K key, V oldValue, V newValue)
	{
		return map.replace(key, oldValue, newValue);
	}


	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#replace(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V replace(K key, V value)
	{
		return map.replace(key, value);
	}


	/**
	 * @param key
	 * @param mappingFunction
	 * @return
	 * @see java.util.Map#computeIfAbsent(java.lang.Object, java.util.function.Function)
	 */
	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
	{
		return map.computeIfAbsent(key, mappingFunction);
	}


	/**
	 * @param key
	 * @param remappingFunction
	 * @return
	 * @see java.util.Map#computeIfPresent(java.lang.Object, java.util.function.BiFunction)
	 */
	@Override
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
	{
		return map.computeIfPresent(key, remappingFunction);
	}


	/**
	 * @param key
	 * @param remappingFunction
	 * @return
	 * @see java.util.Map#compute(java.lang.Object, java.util.function.BiFunction)
	 */
	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
	{
		return map.compute(key, remappingFunction);
	}


	/**
	 * @param key
	 * @param value
	 * @param remappingFunction
	 * @return
	 * @see java.util.Map#merge(java.lang.Object, java.lang.Object, java.util.function.BiFunction)
	 */
	@Override
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
	{
		return map.merge(key, value, remappingFunction);
	}
}
