package space.dcce.commons.netaddr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import space.dcce.commons.general.CollectionUtils;
import space.dcce.commons.general.Range;
import space.dcce.commons.general.RangeSet;


// TODO: Auto-generated Javadoc
/**
 * The Class Addresses.
 */
public class Addresses
{
	
	/** The ranges. */
	private RangeSet ranges;
	
	/** The all addresses. */
	private long[] allAddresses = null;
	
	/** The changed. */
	private boolean changed;
	
	/** The name. */
	private final String name;

	/**
	 * Instantiates a new addresses.
	 *
	 * @param name the name
	 */
	public Addresses(String name)
	{
		this(name, new RangeSet(name));
	}


	/**
	 * Instantiates a new addresses.
	 *
	 * @param name the name
	 * @param ranges the ranges
	 */
	private Addresses(String name, RangeSet ranges)
	{
		this.ranges = ranges;
		this.name = name;
	}


	/**
	 * Adds the.
	 *
	 * @param address the address
	 */
	public void add(SimpleInet4Address address)
	{
		changed = true;
		ranges.add(new Range(address.toLong()));
	}


	/**
	 * Adds the.
	 *
	 * @param range the range
	 * @throws InvalidIPAddressFormatException the invalid IP address format exception
	 */
	@SuppressWarnings("unchecked")
	public void add(String range) throws InvalidIPAddressFormatException
	{
		changed = true;
		ranges.addAll((Collection<Range>) (Collection<?>) IP4Utils.parseAddressRange(range));
	}


	/**
	 * Contains.
	 *
	 * @param address the address
	 * @return true, if successful
	 */
	public boolean contains(SimpleInet4Address address)
	{
		return ranges.contains(address.toLong());
	}


	/**
	 * Gets the CIDR list.
	 *
	 * @return the CIDR list
	 */
	public List<String> getCIDRList()
	{
		List<String> cidrs = new ArrayList<String>();
		for (Range range: ranges.getFlattendRanges())
		{
			cidrs.addAll(IP4Utils.rangeToCIDRs((range.getStart() & 0xFFFFFFFF), (range.getEnd() & 0xFFFFFFFF)));
		}
		return cidrs;
	}

	/**
	 * Gets the CIDR list.
	 *
	 * @param delimiter the delimiter
	 * @return the CIDR list
	 */
	public String getCIDRList(String delimiter)
	{
		return CollectionUtils.joinObjects(delimiter, getCIDRList());
	}


	/**
	 * Gets the all addresses.
	 *
	 * @return the all addresses
	 */
	public synchronized long[] getAllAddresses()
	{
		if (changed)
		{
			allAddresses = new long[ranges.size()];
			int index = 0;
			for (Range range : ranges.getFlattendRanges())
			{
				for (long value = range.getStart(); value <= range.getEnd(); value++)
				{
					allAddresses[index++] = (value & 0xFFFFFFFF);
				}
			}
			changed = false;
		}
		return allAddresses;
	}


	/**
	 * Complement.
	 *
	 * @return the addresses
	 */
	public Addresses complement()
	{
		return new Addresses("Complement of " + name, ranges.complement());
	}


	/**
	 * Difference.
	 *
	 * @param target the target
	 * @return the addresses
	 */
	public Addresses difference(Addresses target)
	{
		return new Addresses("Difference of " + name + " and " + target.name, ranges.difference(target.ranges));
	}


	/**
	 * Intersection.
	 *
	 * @param target the target
	 * @return the addresses
	 */
	public Addresses intersection(Addresses target)
	{
		return new Addresses("Intersection of " + name + " and " + target.name, ranges.intersection(target.ranges));
	}


	/**
	 * Adds the.
	 *
	 * @param range the range
	 */
	public void add(Range range)
	{
		ranges.add(range);
	}


	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty()
	{
		return ranges.isEmpty();
	}


	/**
	 * Ranges to I ps.
	 *
	 * @param ranges the ranges
	 * @return the string
	 */
	private static String rangesToIPs(List<Range> ranges)
	{
		StringBuilder sb = new StringBuilder();

		boolean first = true;
		for (Range range : ranges)
		{
			if (!first)
			{
				sb.append(", ");
			}
			else
			{
				first = false;
			}
			sb.append(IP4Utils.longToString(range.getStart())).append("-");
			sb.append(IP4Utils.longToString(range.getEnd()));
		}
		return sb.toString();
	}


	/**
	 * To string.
	 *
	 * @param includeCombined the include combined
	 * @return the string
	 */
	public String toString(boolean includeCombined)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("provided (" + ranges.size() + "): ").append(rangesToIPs(ranges.getOriginalRanges()));
		if (includeCombined)
		{
			sb.append("\ncombined: ").append(rangesToIPs(ranges.getFlattendRanges()));
		}
		return sb.toString();

	}


	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return toString(true);
	}
}
