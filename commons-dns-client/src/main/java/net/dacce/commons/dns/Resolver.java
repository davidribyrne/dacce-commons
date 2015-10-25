package net.dacce.commons.dns;

import net.dacce.commons.netaddr.SimpleInetAddress;

import org.apache.directory.server.dns.messages.QuestionRecord;
import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;
import org.apache.directory.server.dns.messages.ResourceRecord;
import org.apache.directory.server.dns.store.RecordStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Resolver
{
	private final static Logger logger = LoggerFactory.getLogger(Resolver.class);
	private List<InetSocketAddress> upstreamServers;
    private final SimpleDnsCache cache = new SimpleDnsCache();

	public Resolver()
	{
		try
		{
			InetAddress.getByName("");
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<SimpleInetAddress> quickResolve(String hostname, boolean useCache)
	{
		QuestionRecord question = new QuestionRecord(hostname, RecordType.A, RecordClass.IN);
		
		return null;
	}
	
	public ResourceRecord query(QuestionRecord question, boolean useCache)
	{
		return null;
	}
	
	public void asyncQuery(List<DnsTransaction> questions, BulkDnsCallback callback, boolean useCache)
	{
		
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}

	

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}


}
