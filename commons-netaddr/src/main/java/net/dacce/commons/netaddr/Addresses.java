package net.dacce.commons.netaddr;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import net.dacce.commons.general.CollectionUtils;
import net.dacce.commons.general.StringUtils;
import net.dacce.commons.general.UniqueList;


public class Addresses
{
	private final List<String> blocks;
	private final List<SimpleInetAddress> addresses;


	public Addresses()
	{
		blocks = new UniqueList<String>();
		addresses = new UniqueList<SimpleInetAddress>();
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
		return addresses;
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
