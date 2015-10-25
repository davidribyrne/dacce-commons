package net.dacce.commons.dns;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.directory.server.dns.messages.QuestionRecord;
import org.apache.directory.server.dns.messages.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DnsCacheKey
{
	private final static Logger logger = LoggerFactory.getLogger(DnsCacheKey.class);

	final public String name;
	final public RecordType recordType;
	
	public DnsCacheKey(QuestionRecord question)
	{
		name = question.getDomainName();
		recordType = question.getRecordType();
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(" (");
		sb.append(recordType.toString());
		sb.append(")");
		return sb.toString();
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(name).append(recordType).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		DnsCacheKey ck = (DnsCacheKey) obj;
		return new EqualsBuilder().append(name, ck.name).append(recordType, ck.recordType).isEquals();
	}


}
