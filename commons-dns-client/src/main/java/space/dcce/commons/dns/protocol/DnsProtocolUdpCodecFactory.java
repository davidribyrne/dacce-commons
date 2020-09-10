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

package space.dcce.commons.dns.protocol;


import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


// TODO: Auto-generated Javadoc
/**
 * A factory for creating DnsProtocolUdpCodec objects.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class DnsProtocolUdpCodecFactory implements ProtocolCodecFactory
{
    
    /** The Constant INSTANCE. */
    private static final DnsProtocolUdpCodecFactory INSTANCE = new DnsProtocolUdpCodecFactory();


    /**
     * Returns the singleton instance of {@link DnsProtocolUdpCodecFactory}.
     *
     * @return The singleton instance of {@link DnsProtocolUdpCodecFactory}.
     */
    public static DnsProtocolUdpCodecFactory getInstance()
    {
        return INSTANCE;
    }


    /**
     * Instantiates a new dns protocol udp codec factory.
     */
    private DnsProtocolUdpCodecFactory()
    {
        // Private constructor prevents instantiation outside this class.
    }


    /**
     * Gets the encoder.
     *
     * @param session the session
     * @return the encoder
     */
    public ProtocolEncoder getEncoder( IoSession session )
    {
        // Create a new encoder.
        return new DnsUdpEncoder();
    }


    /**
     * Gets the decoder.
     *
     * @param session the session
     * @return the decoder
     */
    public ProtocolDecoder getDecoder( IoSession session )
    {
        // Create a new decoder.
        return new DnsUdpDecoder();
    }
}
