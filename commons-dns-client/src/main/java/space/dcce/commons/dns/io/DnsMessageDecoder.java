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

package space.dcce.commons.dns.io;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.dcce.commons.dns.messages.DnsMessage;
import space.dcce.commons.dns.messages.MessageType;
import space.dcce.commons.dns.messages.OpCode;
import space.dcce.commons.dns.messages.QuestionRecord;
import space.dcce.commons.dns.messages.RecordClass;
import space.dcce.commons.dns.messages.ResponseCode;
import space.dcce.commons.dns.records.RecordFactory;
import space.dcce.commons.dns.records.RecordType;
import space.dcce.commons.dns.records.ResourceRecord;


// TODO: Auto-generated Javadoc
/**
 * A decoder for DNS messages.  The primary usage of the DnsMessageDecoder is by
 * calling the <code>decode(ByteBuffer)</code> method which will read the 
 * message from the incoming ByteBuffer and build a <code>DnsMessage</code>
 * from it according to the DnsMessage encoding in RFC-1035.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DnsMessageDecoder
{

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger( DnsMessageDecoder.class );



    /**
     * Decode the {@link IoBuffer} into a {@link DnsMessage}.
     *
     * @param in the in
     * @return The {@link DnsMessage}.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public DnsMessage decode( IoBuffer in ) throws IOException
    {
        DnsMessage message = new DnsMessage();

        message.setTransactionId( in.getUnsignedShort() );

        byte header = in.get();
        message.setMessageType( decodeMessageType( header ) );
        message.setOpCode( decodeOpCode( header ) );
        message.setAuthoritativeAnswer( decodeAuthoritativeAnswer( header ) );
        message.setTruncated( decodeTruncated( header ) );
        message.setRecursionDesired( decodeRecursionDesired( header ) );

        header = in.get();
        message.setRecursionAvailable( decodeRecursionAvailable( header ) );
        message.setResponseCode( decodeResponseCode( header ) );

        short questionCount = in.getShort();
        short answerCount = in.getShort();
        short authorityCount = in.getShort();
        short additionalCount = in.getShort();

        logger.debug( "decoding {} question records", questionCount );
        message.setQuestionRecords( getQuestions( in, questionCount ) );

        logger.debug( "decoding {} answer records", answerCount );
        message.setAnswerRecords( getRecords( in, answerCount ) );

        logger.debug( "decoding {} authority records", authorityCount );
        message.setAuthorityRecords( getRecords( in, authorityCount ) );

        logger.debug( "decoding {} additional records", additionalCount );
        message.setAdditionalRecords( getRecords( in, additionalCount ) );

        return message;
    }


    /**
     * Gets the records.
     *
     * @param byteBuffer the byte buffer
     * @param recordCount the record count
     * @return the records
     */
    private List<ResourceRecord> getRecords( IoBuffer byteBuffer, short recordCount )
    {
        List<ResourceRecord> records = new ArrayList<ResourceRecord>( recordCount );

        for ( int ii = 0; ii < recordCount; ii++ )
        {
            String domainName = getDomainName( byteBuffer );
            RecordType recordType = RecordType.convert( byteBuffer.getShort() );
            RecordClass recordClass = RecordClass.convert( byteBuffer.getShort() );

            int timeToLive = byteBuffer.getInt();
            short dataLength = byteBuffer.getShort();
            
            ResourceRecord record = RecordFactory.makeResourceRecord(recordType, domainName, recordClass, timeToLive);
            record.decodeData(byteBuffer, dataLength);
            records.add(record);
        }

        return records;
    }




    /**
     * Gets the questions.
     *
     * @param byteBuffer the byte buffer
     * @param questionCount the question count
     * @return the questions
     */
    private List<QuestionRecord> getQuestions( IoBuffer byteBuffer, short questionCount )
    {
        List<QuestionRecord> questions = new ArrayList<QuestionRecord>( questionCount );

        for ( int ii = 0; ii < questionCount; ii++ )
        {
            String domainName = getDomainName( byteBuffer );

            RecordType recordType = RecordType.convert( byteBuffer.getShort() );
            RecordClass recordClass = RecordClass.convert( byteBuffer.getShort() );

            questions.add( new QuestionRecord( domainName, recordType, recordClass ) );
        }

        return questions;
    }


    /**
     * Gets the domain name.
     *
     * @param byteBuffer the byte buffer
     * @return the domain name
     */
    static String getDomainName( IoBuffer byteBuffer )
    {
        StringBuffer domainName = new StringBuffer();
        recurseDomainName( byteBuffer, domainName );

        return domainName.toString();
    }


    /**
     * Recurse domain name.
     *
     * @param byteBuffer the byte buffer
     * @param domainName the domain name
     */
    static void recurseDomainName( IoBuffer byteBuffer, StringBuffer domainName )
    {
        int length = byteBuffer.getUnsigned();

        if ( isOffset( length ) )
        {
            int position = byteBuffer.getUnsigned();
            int offset = length & ~( 0xc0 ) << 8;
            int originalPosition = byteBuffer.position();
            byteBuffer.position( position + offset );

            recurseDomainName( byteBuffer, domainName );

            byteBuffer.position( originalPosition );
        }
        else if ( isLabel( length ) )
        {
            int labelLength = length;
            getLabel( byteBuffer, domainName, labelLength );
            recurseDomainName( byteBuffer, domainName );
        }
    }


    /**
     * Checks if is offset.
     *
     * @param length the length
     * @return true, if is offset
     */
    static boolean isOffset( int length )
    {
        return ( ( length & 0xc0 ) == 0xc0 );
    }


    /**
     * Checks if is label.
     *
     * @param length the length
     * @return true, if is label
     */
    static boolean isLabel( int length )
    {
        return ( length != 0 && ( length & 0xc0 ) == 0 );
    }


    /**
     * Gets the label.
     *
     * @param byteBuffer the byte buffer
     * @param domainName the domain name
     * @param labelLength the label length
     */
    static void getLabel( IoBuffer byteBuffer, StringBuffer domainName, int labelLength )
    {
        for ( int jj = 0; jj < labelLength; jj++ )
        {
            char character = ( char ) byteBuffer.get();
            domainName.append( character );
        }

        if ( byteBuffer.get( byteBuffer.position() ) != 0 )
        {
            domainName.append( "." );
        }
    }


    /**
     * Decode message type.
     *
     * @param header the header
     * @return the message type
     */
    private MessageType decodeMessageType( byte header )
    {
        return MessageType.convert( ( byte ) ( ( header & 0x80 ) >>> 7 ) );
    }


    /**
     * Decode op code.
     *
     * @param header the header
     * @return the op code
     */
    private OpCode decodeOpCode( byte header )
    {
        return OpCode.convert( ( byte ) ( ( header & 0x78 ) >>> 3 ) );
    }


    /**
     * Decode authoritative answer.
     *
     * @param header the header
     * @return true, if successful
     */
    private boolean decodeAuthoritativeAnswer( byte header )
    {
        return ( ( header & 0x04 ) >>> 2 ) == 1;
    }


    /**
     * Decode truncated.
     *
     * @param header the header
     * @return true, if successful
     */
    private boolean decodeTruncated( byte header )
    {
        return ( ( header & 0x02 ) >>> 1 ) == 1;
    }


    /**
     * Decode recursion desired.
     *
     * @param header the header
     * @return true, if successful
     */
    private boolean decodeRecursionDesired( byte header )
    {
        return ( ( header & 0x01 ) ) == 1;
    }


    /**
     * Decode recursion available.
     *
     * @param header the header
     * @return true, if successful
     */
    private boolean decodeRecursionAvailable( byte header )
    {
        return ( ( header & 0x80 ) >>> 7 ) == 1;
    }


    /**
     * Decode response code.
     *
     * @param header the header
     * @return the response code
     */
    private ResponseCode decodeResponseCode( byte header )
    {
        return ResponseCode.convert( ( byte ) ( header & 0x0F ) );
    }
}
