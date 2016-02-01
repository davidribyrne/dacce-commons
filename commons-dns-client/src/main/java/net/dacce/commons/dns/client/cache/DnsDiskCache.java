package net.dacce.commons.dns.client.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.esotericsoftware.yamlbeans.YamlException;
import net.dacce.commons.dns.messages.QuestionRecord;
import net.dacce.commons.dns.records.ResourceRecord;
import net.dacce.commons.general.YamlUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class DnsDiskCache implements DnsCache
{
	private final static Logger logger = LoggerFactory.getLogger(DnsDiskCache.class);
	
	private String filename;
	private SimpleDnsCache internalCache;
	
	public DnsDiskCache(String filename)
	{
		this.filename = filename;
		
		try
		{
			internalCache = (SimpleDnsCache) YamlUtils.readObject(filename, SimpleDnsCache.class);
		}
		catch (FileNotFoundException e)
		{
			logger.trace("DNS cache yaml file not found (" + filename + "). Creating new object.", e);
		}
		catch (YamlException e)
		{
			logger.warn("Problem reading YAML for DNS cache: " + e.getLocalizedMessage(), e);
		}
		if (internalCache == null)
			internalCache = new SimpleDnsCache();
	}

	public void writeToDisk() throws YamlException, IOException
	{
		YamlUtils.writeObject(filename, internalCache);
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

	public List<ResourceRecord> get(QuestionRecord question)
	{
		return internalCache.get(question);
	}

	public void add(QuestionRecord question, List<ResourceRecord> records)
	{
		internalCache.add(question, records);
	}

	public boolean contains(QuestionRecord question)
	{
		return internalCache.contains(question);
	}

	public List<ResourceRecord> getAny(String domainName)
	{
		return internalCache.getAny(domainName);
	}


}
