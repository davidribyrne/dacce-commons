package net.dacce.commons.general;

import java.util.Collection;


// TODO: Auto-generated Javadoc
/**
 * The Class CollectionUtils.
 */
public class CollectionUtils
{

	/**
	 * Instantiates a new collection utils.
	 */
	private CollectionUtils()
	{
	}


	/**
	 * Join objects using toString()
	 *
	 * @param delimiter
	 *            the delimiter
	 * @param objects
	 *            the objects
	 * @return the string
	 */
	public static String joinObjects(String delimiter, Iterable<?> objects)
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Object object : objects)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(delimiter);
			}
			sb.append(object.toString());
		}
		return sb.toString();
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addArray(Collection collection, Object[] array)
	{
		for (Object o : array)
		{
			collection.add(o);
		}
	}

}
