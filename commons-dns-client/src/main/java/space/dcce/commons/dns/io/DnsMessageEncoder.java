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


import java.util.Iterator;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import space.dcce.commons.dns.messages.DnsMessage;
import space.dcce.commons.dns.messages.MessageType;
import space.dcce.commons.dns.messages.OpCode;
import space.dcce.commons.dns.messages.QuestionRecord;
import space.dcce.commons.dns.messages.ResponseCode;
import space.dcce.commons.dns.records.ResourceRecord;


// TODO: Auto-generated Javadoc
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
     * @param byteBuffer the byte buffer
     * @param message the message
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


    /**
     * Put question records.
     *
     * @param byteBuffer the byte buffer
     * @param questions the questions
     */
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


    /**
     * Put resource records.
     *
     * @param byteBuffer the byte buffer
     * @param records the records
     */
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


    /**
     * Encode message type.
     *
     * @param messageType the message type
     * @return the byte
     */
    private byte encodeMessageType( MessageType messageType )
    {
        byte oneBit = ( byte ) ( messageType.convert() & 0x01 );
        return ( byte ) ( oneBit << 7 );
    }


    /**
     * Encode op code.
     *
     * @param opCode the op code
     * @return the byte
     */
    private byte encodeOpCode( OpCode opCode )
    {
        byte fourBits = ( byte ) ( opCode.convert() & 0x0F );
        return ( byte ) ( fourBits << 3 );
    }


    /**
     * Encode authoritative answer.
     *
     * @param authoritative the authoritative
     * @return the byte
     */
    private byte encodeAuthoritativeAnswer( boolean authoritative )
    {
        if ( authoritative )
        {
            return ( byte ) ( ( byte ) 0x01 << 2 );
        }
        return ( byte ) 0;
    }


    /**
     * Encode truncated.
     *
     * @param truncated the truncated
     * @return the byte
     */
    private byte encodeTruncated( boolean truncated )
    {
        if ( truncated )
        {
            return ( byte ) ( ( byte ) 0x01 << 1 );
        }
        return 0;
    }


    /**
     * Encode recursion desired.
     *
     * @param recursionDesired the recursion desired
     * @return the byte
     */
    private byte encodeRecursionDesired( boolean recursionDesired )
    {
        if ( recursionDesired )
        {
            return ( byte ) 0x01;
        }
        return 0;
    }


    /**
     * Encode recursion available.
     *
     * @param recursionAvailable the recursion available
     * @return the byte
     */
    private byte encodeRecursionAvailable( boolean recursionAvailable )
    {
        if ( recursionAvailable )
        {
            return ( byte ) ( ( byte ) 0x01 << 7 );
        }
        return 0;
    }


    /**
     * Encode response code.
     *
     * @param responseCode the response code
     * @return the byte
     */
    private byte encodeResponseCode( ResponseCode responseCode )
    {
        return ( byte ) ( responseCode.convert() & 0x0F );
    }
}
