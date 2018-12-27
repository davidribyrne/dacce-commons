//package space.dcce.commons.netaddr;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import space.dcce.commons.general.UnexpectedException;
//
//import java.util.*;
//
//public class IP4SubnetList
//{
//	private final static Logger logger = LoggerFactory.getLogger(IP4SubnetList.class);
//
//	private static class Subnet
//	{
//		public Subnet(int network, byte mask)
//		{
//			this.network = network;
//			this.mask = mask;
//		}
//		int network;
//		byte mask;
//		
//		boolean contains(int address)
//		{
//			return network == (IP4Utils.getNetMask(mask) & address);
//		}
//	}
//	private Subnet[] subnets;
//	
//	private static Comparator<Subnet> sc = new Comparator<IP4SubnetList.Subnet>()
//	{
//		@Override
//		public int compare(Subnet o1, Subnet o2)
//		{
//			return o1.network - o2.network;
//		}
//	};
//
//	
//	public IP4SubnetList()
//	{
//		subnets = new Subnet[]{};
//	}
//	
//	private static Subnet parseCidr(String cidr) throws InvalidIPAddressFormatException
//	{
//		if (!IP4Utils.validateCidr(cidr))
//		{
//			throw new InvalidIPAddressFormatException("Invalid CIDR format (" + cidr + ")");
//		}
//		
//		String parts[] = cidr.split("/");
//		int mask = Integer.parseInt(parts[1]);
//		int count = (int) Math.pow(2, (32 - mask));
//		int base = IP4Utils.stringToInt(parts[0]);
//		if ((base % count) != 0)
//		{
//			throw new InvalidIPAddressFormatException("Base address is not valid for netmask (" + cidr + ")");
//		}
//		return new Subnet(base, (byte) mask);
//	}
//	
//	public boolean contains(SimpleInet4Address address)
//	{
//		Subnet subAddress = new Subnet(address.toInt(), (byte) 32);
//		int result = Arrays.binarySearch(subnets, subAddress, sc);
//		return subnets[result].contains(address.toInt());
//	}
//	
//	public void add(String[] lines) throws InvalidIPAddressFormatException
//	{
//		int position = subnets.length;
//		subnets = Arrays.copyOf(subnets, subnets.length + lines.length);
//		
//		for (String line: lines)
//		{
//			subnets[position++] = parseCidr(line);
//		}
//		sort();
//	}
//	
//	
//	private void sort() 
//	{
//		Arrays.sort(subnets, sc);
//	}
//}
