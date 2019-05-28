package space.dcce.commons.netaddr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import space.dcce.commons.general.CollectionUtils;
import space.dcce.commons.general.Range;
import space.dcce.commons.general.RangeSet;


public class Addresses
{
	private RangeSet ranges;
	private long[] allAddresses = null;
	private boolean changed;
	private final String name;

	public Addresses(String name)
	{
		this(name, new RangeSet(name));
	}


	private Addresses(String name, RangeSet ranges)
	{
		this.ranges = ranges;
		this.name = name;
	}


	public void add(SimpleInet4Address address)
	{
		changed = true;
		ranges.add(new Range(address.toLong()));
	}


	@SuppressWarnings("unchecked")
	public void add(String range) throws InvalidIPAddressFormatException
	{
		changed = true;
		ranges.addAll((Collection<Range>) (Collection<?>) IP4Utils.parseAddressRange(range));
	}


	public boolean contains(SimpleInet4Address address)
	{
		return ranges.contains(address.toLong());
	}


	public List<String> getCIDRList()
	{
		List<String> cidrs = new ArrayList<String>();
		for (Range range: ranges.getFlattendRanges())
		{
			cidrs.addAll(IP4Utils.rangeToCIDRs((range.getStart() & 0xFFFFFFFF), (range.getEnd() & 0xFFFFFFFF)));
		}
		return cidrs;
	}

	public String getCIDRList(String delimiter)
	{
		return CollectionUtils.joinObjects(delimiter, getCIDRList());
	}


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


	public Addresses complement()
	{
		return new Addresses("Complement of " + name, ranges.complement());
	}


	public Addresses difference(Addresses target)
	{
		return new Addresses("Difference of " + name + " and " + target.name, ranges.difference(target.ranges));
	}


	public Addresses intersection(Addresses target)
	{
		return new Addresses("Intersection of " + name + " and " + target.name, ranges.intersection(target.ranges));
	}


	public void add(Range range)
	{
		ranges.add(range);
	}


	public boolean isEmpty()
	{
		return ranges.isEmpty();
	}


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


	@Override
	public String toString()
	{
		return toString(true);
	}
}
