package space.dcce.commons.netaddr;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.dcce.commons.general.CollectionUtils;
import space.dcce.commons.general.Range;
import space.dcce.commons.general.RangeSet;
import space.dcce.commons.general.UnexpectedException;
import space.dcce.commons.general.UniqueList;


public class Addresses
{
	private RangeSet ranges;
	private int[] allAddresses = null;
	private boolean changed;


	public Addresses()
	{
		this(new RangeSet());
	}


	private Addresses(RangeSet ranges)
	{
		this.ranges = ranges;
	}


	public void add(SimpleInet4Address address)
	{
		changed = true;
		ranges.add(new Range(address.toInt()));
	}


	@SuppressWarnings("unchecked")
	public void add(String range) throws InvalidIPAddressFormatException
	{
		changed = true;
		ranges.addAll((Collection<Range>) (Collection<?>) IP4Utils.parseAddressRange(range));
	}


	public boolean contains(SimpleInet4Address address)
	{
		return ranges.contains(address.toInt());
	}


	public String getNmapList()
	{
		return CollectionUtils.joinObjects("\n", ranges.getFlattendRanges());
	}


	public synchronized int[] getAllAddresses()
	{
		if (changed)
		{
			allAddresses = new int[ranges.size()];
			int index = 0;
			for (Range range : ranges.getFlattendRanges())
			{
				for (long value = range.getStart(); value <= range.getEnd(); value++)
				{
					allAddresses[index++] = (int) (value & 0xFFFFFFFF);
				}
			}
			changed = false;
		}
		return allAddresses;
	}


	public Addresses complement()
	{
		return new Addresses(ranges.complement());
	}


	public Addresses difference(Addresses target)
	{
		return new Addresses(ranges.difference(target.ranges));
	}


	public Addresses intersection(Addresses target)
	{
		return new Addresses(ranges.intersection(target.ranges));
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
			sb.append(IP4Utils.intToString((int) range.getStart())).append("-");
			sb.append(IP4Utils.intToString((int) range.getEnd()));
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
