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

package net.dacce.commons.dns.io;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.dacce.commons.dns.messages.DnsMessage;
import net.dacce.commons.dns.messages.MessageType;
import net.dacce.commons.dns.messages.OpCode;
import net.dacce.commons.dns.messages.QuestionRecord;
import net.dacce.commons.dns.messages.RecordClass;
import net.dacce.commons.dns.messages.ResponseCode;
import net.dacce.commons.dns.records.RecordFactory;
import net.dacce.commons.dns.records.RecordType;
import net.dacce.commons.dns.records.ResourceRecord;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    private final Logger logger = LoggerFactory.getLogger( DnsMessageDecoder.class );



    /**
     * Decode the {@link IoBuffer} into a {@link DnsMessage}.
     *
     * @param in
     * @return The {@link DnsMessage}.
     * @throws IOException
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


    static String getDomainName( IoBuffer byteBuffer )
    {
        StringBuffer domainName = new StringBuffer();
        recurseDomainName( byteBuffer, domainName );

        return domainName.toString();
    }


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


    static boolean isOffset( int length )
    {
        return ( ( length & 0xc0 ) == 0xc0 );
    }


    static boolean isLabel( int length )
    {
        return ( length != 0 && ( length & 0xc0 ) == 0 );
    }


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


    private MessageType decodeMessageType( byte header )
    {
        return MessageType.convert( ( byte ) ( ( header & 0x80 ) >>> 7 ) );
    }


    private OpCode decodeOpCode( byte header )
    {
        return OpCode.convert( ( byte ) ( ( header & 0x78 ) >>> 3 ) );
    }


    private boolean decodeAuthoritativeAnswer( byte header )
    {
        return ( ( header & 0x04 ) >>> 2 ) == 1;
    }


    private boolean decodeTruncated( byte header )
    {
        return ( ( header & 0x02 ) >>> 1 ) == 1;
    }


    private boolean decodeRecursionDesired( byte header )
    {
        return ( ( header & 0x01 ) ) == 1;
    }


    private boolean decodeRecursionAvailable( byte header )
    {
        return ( ( header & 0x80 ) >>> 7 ) == 1;
    }


    private ResponseCode decodeResponseCode( byte header )
    {
        return ResponseCode.convert( ( byte ) ( header & 0x0F ) );
    }
}
