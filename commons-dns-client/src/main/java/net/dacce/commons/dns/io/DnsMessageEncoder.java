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


import java.util.Iterator;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import net.dacce.commons.dns.messages.DnsMessage;
import net.dacce.commons.dns.messages.MessageType;
import net.dacce.commons.dns.messages.OpCode;
import net.dacce.commons.dns.messages.QuestionRecord;
import net.dacce.commons.dns.messages.ResponseCode;
import net.dacce.commons.dns.records.ResourceRecord;


/**
 * An encoder for DNS messages.  The primary usage of the DnsMessageEncoder is 
 * to call the <code>encode(ByteBuffer, DnsMessage)</code> method which will 
 * write the message to the outgoing ByteBuffer according to the DnsMessage 
 * encoding in RFC-1035.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DnsMessageEncoder
{

    /**
     * Encodes the {@link DnsMessage} into the {@link IoBuffer}.
     *
     * @param byteBuffer
     * @param message
     */
    public void encode( IoBuffer byteBuffer, DnsMessage message )
    {
        byteBuffer.putShort( ( short ) message.getTransactionId() );

        byte header = ( byte ) 0x00;
        header |= encodeMessageType( message.getMessageType() );
        header |= encodeOpCode( message.getOpCode() );
        header |= encodeAuthoritativeAnswer( message.isAuthoritativeAnswer() );
        header |= encodeTruncated( message.isTruncated() );
        header |= encodeRecursionDesired( message.isRecursionDesired() );
        byteBuffer.put( header );

        header = ( byte ) 0x00;
        header |= encodeRecursionAvailable( message.isRecursionAvailable() );
        header |= encodeResponseCode( message.getResponseCode() );
        byteBuffer.put( header );

        byteBuffer
            .putShort( ( short ) ( message.getQuestionRecords() != null ? message.getQuestionRecords().size() : 0 ) );
        byteBuffer.putShort( ( short ) ( message.getAnswerRecords() != null ? message.getAnswerRecords().size() : 0 ) );
        byteBuffer.putShort( ( short ) ( message.getAuthorityRecords() != null ? message.getAuthorityRecords().size()
            : 0 ) );
        byteBuffer.putShort( ( short ) ( message.getAdditionalRecords() != null ? message.getAdditionalRecords().size()
            : 0 ) );

        putQuestionRecords( byteBuffer, message.getQuestionRecords() );
        putResourceRecords( byteBuffer, message.getAnswerRecords() );
        putResourceRecords( byteBuffer, message.getAuthorityRecords() );
        putResourceRecords( byteBuffer, message.getAdditionalRecords() );
    }


    private void putQuestionRecords( IoBuffer byteBuffer, List<QuestionRecord> questions )
    {
        if ( questions == null )
        {
            return;
        }

        QuestionEncoder encoder = new QuestionEncoder();

        Iterator<QuestionRecord> it = questions.iterator();

        while ( it.hasNext() )
        {
            QuestionRecord question = it.next();
            encoder.put( byteBuffer, question );
        }
    }


    private void putResourceRecords( IoBuffer byteBuffer, List<ResourceRecord> records )
    {
        if ( records == null )
        {
            return;
        }

        Iterator<ResourceRecord> it = records.iterator();

        while ( it.hasNext() )
        {
            ResourceRecord record = it.next();
            record.encode(byteBuffer);
        }
    }


    private byte encodeMessageType( MessageType messageType )
    {
        byte oneBit = ( byte ) ( messageType.convert() & 0x01 );
        return ( byte ) ( oneBit << 7 );
    }


    private byte encodeOpCode( OpCode opCode )
    {
        byte fourBits = ( byte ) ( opCode.convert() & 0x0F );
        return ( byte ) ( fourBits << 3 );
    }


    private byte encodeAuthoritativeAnswer( boolean authoritative )
    {
        if ( authoritative )
        {
            return ( byte ) ( ( byte ) 0x01 << 2 );
        }
        return ( byte ) 0;
    }


    private byte encodeTruncated( boolean truncated )
    {
        if ( truncated )
        {
            return ( byte ) ( ( byte ) 0x01 << 1 );
        }
        return 0;
    }


    private byte encodeRecursionDesired( boolean recursionDesired )
    {
        if ( recursionDesired )
        {
            return ( byte ) 0x01;
        }
        return 0;
    }


    private byte encodeRecursionAvailable( boolean recursionAvailable )
    {
        if ( recursionAvailable )
        {
            return ( byte ) ( ( byte ) 0x01 << 7 );
        }
        return 0;
    }


    private byte encodeResponseCode( ResponseCode responseCode )
    {
        return ( byte ) ( responseCode.convert() & 0x0F );
    }
}
