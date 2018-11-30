package space.dcce.commons.netaddr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import space.dcce.commons.general.CollectionUtils;
import space.dcce.commons.general.UniqueList;


public class Addresses
{
	private final List<String> blocks;
	private final ArrayList<SimpleInetAddress> addresses;


	public Addresses(int size)
	{
		blocks = new UniqueList<String>(false);
		addresses = new UniqueList<SimpleInetAddress>(false, size);
	}
	
	public int size()
	{
		return addresses.size();
	}
	
	public void trimToSize()
	{
		addresses.trimToSize();
	}

	public boolean isEmpty()
	{
		return blocks.isEmpty();
	}

	public List<String> getBlocks()
	{
		return blocks;
	}


	public void add(String block) throws InvalidIPAddressFormatException
	{
		blocks.add(block);
		addresses.addAll(IPUtils.parseAddressBlock(block));
	}


	public void add(Collection<SimpleInetAddress> block)
	{
		for (SimpleInetAddress address : block)
		{
			blocks.add(address.toString());
		}
		addresses.addAll(block);
	}


	public void add(SimpleInetAddress address)
	{
		blocks.add(address.toString());
		addresses.add(address);
	}


	public boolean contains(SimpleInetAddress address)
	{
		return addresses.contains(address);
	}


	public List<SimpleInetAddress> getAddresses()
	{
		return Collections.unmodifiableList(addresses);
	}


	public String getNmapList()
	{
		return CollectionUtils.joinObjects("\n", addresses);
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append(CollectionUtils.joinObjects(",", blocks))
				.append("Expanded addresses", CollectionUtils.joinObjects(",", addresses))
				.build();
	}
}
