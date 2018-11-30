package space.dcce.commons.netaddr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import space.dcce.commons.general.CollectionUtils;
import space.dcce.commons.general.UnexpectedException;
import space.dcce.commons.general.UniqueList;


public class Addresses
{
	private final List<IP4Range> blocks;
	private boolean changed;
	private int[] allAddresses;


	public Addresses()
	{
		blocks = new UniqueList<IP4Range>(false);
	}


	// TODO Improve size function
	/**
	 * Simple counting. Doesn't identify duplicate addresses.
	 * 
	 * @return
	 */
	public int roughSize()
	{
		int size = 0;
		for (IP4Range block : blocks)
		{
			size += block.size();
		}
		return size;
	}


	public int[] getAllAddresses()
	{
		synchronized (this)
		{

			if (changed || allAddresses == null)
			{
				Set<Integer> addresses = new HashSet<Integer>();

				for (IP4Range range : blocks)
				{
					for (int i = range.getStart(); i <= range.getEnd(); i++)
					{
						addresses.add(i);
					}
				}

				allAddresses = new int[addresses.size()];
				int pos = 0;
				for(Integer i: addresses)
				{
					allAddresses[pos++] = i;
				}
			}
		}
		return allAddresses;

	}


	public boolean isEmpty()
	{
		return blocks.isEmpty();
	}


	public List<IP4Range> getBlocks()
	{
		return Collections.unmodifiableList(blocks);
	}


	public void add(String block) throws InvalidIPAddressFormatException
	{
		add(IP4Utils.parseAddressBlock(block));
	}


	public void add(Collection<IP4Range> block)
	{
		changed = true;
		blocks.addAll(block);
	}


	public void add(IP4Range block)
	{
		blocks.add(block);
		changed = true;
	}


	public void add(SimpleInet4Address address)
	{
		blocks.add(new IP4Range(address));
		changed = true;
	}


	public boolean contains(SimpleInet4Address address)
	{
		for (IP4Range range : blocks)
		{
			if (range.contains(address))
				return true;
		}
		return false;
	}


	// public List<SimpleInetAddress> getAddresses()
	// {
	// return Collections.unmodifiableList(addresses);
	// }


	public String getNmapList()
	{
		return CollectionUtils.joinObjects("\n", blocks);
	}


	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append(CollectionUtils.joinObjects(",", blocks))
				.build();
	}
}
