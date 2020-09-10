package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;
import space.dcce.commons.general.NotImplementedException;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Record objects.
 */
public class RecordFactory
{

	/**
	 * Instantiates a new record factory.
	 */
	private RecordFactory()
	{
	}
	
	/**
	 * Make resource record.
	 *
	 * @param recordType the record type
	 * @param domainName the domain name
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 * @return the resource record
	 * @throws NotImplementedException the not implemented exception
	 */
	public static ResourceRecord makeResourceRecord(RecordType recordType, String domainName, RecordClass recordClass, int timeToLive) throws NotImplementedException
	{
		
		switch(recordType)
		{
			case A: return new ARecord(domainName, recordClass, timeToLive);
			case AAAA: return new AaaaRecord(domainName, recordClass, timeToLive);
			case MX: return new MxRecord(domainName, recordClass, timeToLive);
			case CNAME: return new CnameRecord(domainName, recordClass, timeToLive);
			case NS: return new NsRecord(domainName, recordClass, timeToLive);
			case PTR: return new PtrRecord(domainName, recordClass, timeToLive);
			case SOA: return new SoaRecord(domainName, recordClass, timeToLive);
			case SRV: return new SrvRecord(domainName, recordClass, timeToLive);
			case TXT: return new TxtRecord(domainName, recordClass, timeToLive);
			
			default:
				return new GenericRecord(domainName, recordType, recordClass, timeToLive);
		}
	}
}
