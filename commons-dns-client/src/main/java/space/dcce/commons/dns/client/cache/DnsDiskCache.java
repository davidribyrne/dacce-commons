package space.dcce.commons.dns.client.cache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.yamlbeans.YamlException;

import space.dcce.commons.dns.messages.QuestionRecord;
import space.dcce.commons.dns.records.ResourceRecord;
import space.dcce.commons.general.YamlUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DnsDiskCache.
 */
public class DnsDiskCache implements DnsCache
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(DnsDiskCache.class);
	
	/** The add count. */
	private int addCount = 0;
	
	/** The filename. */
	private String filename;
	
	/** The internal cache. */
	private SimpleDnsCache internalCache;
	
	/**
	 * Instantiates a new dns disk cache.
	 *
	 * @param filename the filename
	 */
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

	/**
	 * Write to disk.
	 *
	 * @throws YamlException the yaml exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeToDisk() throws YamlException, IOException
	{
		YamlUtils.writeObject(filename, internalCache);
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.append("filename", filename)
				.append("internalCache", internalCache)
				.toString();
	}

//	@Override
//	public int hashCode()
//	{
//		return new HashCodeBuilder()
//				.appendSuper(super.hashCode())
//				.append(filename.hashCode())
//				.append(internalCache.hashCode())
//				.toHashCode();
//	}
//
//	@Override
//	public boolean equals(Object obj)
//	{
//		if (! (obj instanceof DnsDiskCache))
//			return false;
//		
//		DnsDiskCache o = (DnsDiskCache) obj;
//		return new EqualsBuilder()
//				.append(filename, o.filename)
//				.append(internalCache, o.internalCache)
//				.isEquals();
//	}

	/**
 * Gets the.
 *
 * @param question the question
 * @return the list
 */
public List<ResourceRecord> get(QuestionRecord question)
	{
		return internalCache.get(question);
	}

	/**
	 * Adds the.
	 *
	 * @param question the question
	 * @param records the records
	 */
	public void add(QuestionRecord question, List<ResourceRecord> records)
	{
		internalCache.add(question, records);
		if (addCount++ % 10 == 0)
			try
			{
				writeToDisk();
			}
			catch (IOException e)
			{
				logger.error("Failed to save DNS cache to disk.", e);
			}
	}

	/**
	 * Contains.
	 *
	 * @param question the question
	 * @return true, if successful
	 */
	public boolean contains(QuestionRecord question)
	{
		return internalCache.contains(question);
	}

	/**
	 * Gets the any.
	 *
	 * @param domainName the domain name
	 * @return the any
	 */
	public List<ResourceRecord> getAny(String domainName)
	{
		return internalCache.getAny(domainName);
	}


}
