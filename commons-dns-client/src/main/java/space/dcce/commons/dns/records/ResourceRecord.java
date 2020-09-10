/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */

package space.dcce.commons.dns.records;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;

import space.dcce.commons.dns.io.DnsEncodingUtils;
import space.dcce.commons.dns.messages.RecordClass;


// TODO: Auto-generated Javadoc
/**
 * The answer, authority, and additional sections all share the same
 * format: a variable number of resource records, where the number of
 * records is specified in the corresponding count field in the header.
 * Each resource record has the following format:
 *                                     1  1  1  1  1  1
 *       0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *     |                                               |
 *     /                                               /
 *     /                      NAME                     /
 *     |                                               |
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *     |                      TYPE                     |
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *     |                     CLASS                     |
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *     |                      TTL                      |
 *     |                                               |
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *     |                   RDLENGTH                    |
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--|
 *     /                     RDATA                     /
 *     /                                               /
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public abstract class ResourceRecord
{
    /**
     * An owner name, i.e., the name of the node to which this
     * resource record pertains.
     */
    private String domainName;

    /**
     * Two octets containing one of the resource record TYPE codes.
     */
    private RecordType recordType;

    /**
     * Two octets containing one of the resource record CLASS codes.
     * For example, the CLASS field is IN for the Internet.
     */
    private RecordClass recordClass;

    /**
     * A 32 bit signed integer that specifies the time interval
     * that the resource record may be cached before the source
     * of the information should again be consulted.  Zero
     * values are interpreted to mean that the resource record can only be
     * used for the transaction in progress, and should not be
     * cached.  For example, SOA records are always distributed
     * with a zero TTL to prohibit caching.  Zero values can
     * also be used for extremely volatile data.
     */
    private int timeToLive;



    /**
     * Creates a new instance of ResourceRecordImpl.
     *
     * @param domainName the domain name
     * @param recordType the record type
     * @param recordClass the record class
     * @param timeToLive the time to live
     */
    protected ResourceRecord( String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
    {
        this.domainName = domainName;
        this.recordType = recordType;
        this.recordClass = recordClass;
        this.timeToLive = timeToLive;
    }

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("domainName", domainName).append("recordType", recordType)
				.append("recordClass", recordClass).append("TTL", timeToLive).build();
	}


    /**
     * Gets the domain name.
     *
     * @return Returns the domainName.
     */
    public String getDomainName()
    {
        return domainName;
    }


    /**
     * Gets the record type.
     *
     * @return Returns the recordType.
     */
    public RecordType getRecordType()
    {
        return recordType;
    }


    /**
     * Gets the record class.
     *
     * @return Returns the recordClass.
     */
    public RecordClass getRecordClass()
    {
        return recordClass;
    }


    /**
     * Gets the time to live.
     *
     * @return Returns the timeToLive.
     */
    public int getTimeToLive()
    {
        return timeToLive;
    }




    /**
     * Equals.
     *
     * @param o the o
     * @return true, if successful
     */
    public boolean equals( Object o )
    {
    	if (!(o instanceof ResourceRecord))
    		return false;
    	if (o == this)
    		return true;
    	
    	ResourceRecord rr = (ResourceRecord) o;
    	return new EqualsBuilder().append(domainName, rr.domainName).append(recordType, rr.recordType).
    			append(recordClass, rr.recordClass).append(timeToLive, rr.timeToLive).isEquals();
    }


    /**
     * Hash code.
     *
     * @return the int
     */
    public int hashCode()
    {
    	return new HashCodeBuilder().append(domainName).append(recordType).append(recordClass).append(timeToLive).hashCode();
    }
    

    /**
     * Decode data.
     *
     * @param byteBuffer the byte buffer
     * @param length the length
     */
    public abstract void decodeData(IoBuffer byteBuffer, short length);
    
    /**
     * Encode data.
     *
     * @param byteBuffer the byte buffer
     */
    protected abstract void encodeData( IoBuffer byteBuffer);


    /**
     * Encode.
     *
     * @param byteBuffer the byte buffer
     */
    public void encode ( IoBuffer byteBuffer)
    {
    	DnsEncodingUtils.putDomainName( byteBuffer, getDomainName() );
        byteBuffer.putShort( getRecordClass().convert() );

        byteBuffer.putInt( getTimeToLive() );
        byteBuffer.putShort( getRecordType().convert() );
        putResourceRecord(byteBuffer);
    }


    /**
     * Put resource record.
     *
     * @param byteBuffer the byte buffer
     */
    protected void putResourceRecord( IoBuffer byteBuffer )
    {
        int startPosition = byteBuffer.position();
        byteBuffer.position( startPosition + 2 );
        encodeData( byteBuffer );
        DnsEncodingUtils.putDataSize( byteBuffer, startPosition );
    }



}
