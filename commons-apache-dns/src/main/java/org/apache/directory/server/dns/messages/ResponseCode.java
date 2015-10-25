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

package org.apache.directory.server.dns.messages;


import org.apache.directory.server.dns.util.EnumConverter;
import org.apache.directory.server.dns.util.ReverseEnumMap;


/**
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public enum ResponseCode implements EnumConverter<Byte>
{
	
    

    /** No error condition. */
    NO_ERROR(0),

    /** The name server was unable to interpret the query. */
    FORMAT_ERROR(1),

    /** The name server was unable to process this query due to a problem with the name server. */
    SERVER_FAILURE(2),

    /** The domain name referenced in the query does not exist. */
    NAME_ERROR(3),

    /** The name server does not support the requested kind of query. */
    NOT_IMPLEMENTED(4),

    /** The name server refuses to perform the specified operation for policy reasons. */
    REFUSED(5),
    
    /** Name exists when it should not */
	YX_DOMAIN(6), 
	
	/** RR set exists when it should not */
	YX_RR_SET(7),
	
	/** RR set that should exist does not */
	NXRR_SET(8), 
	
	/** Server not authoritative for zone */
	NOT_AUTHORITATIVE(9),
	
	/** Name not contained in zone */
	NOT_IN_ZONE(10),
	// 11-15 are unassigned
	
	// There are two 16s for some reason
	/** Bad OPT version */
	BAD_VERSION(16),

	/** TSIG signature failure */
	BAD_SIGNATURE(16),
	
	/** Key not recognized */
	BAD_KEY(17),
	
	/** Signature out of time window */
	BAD_TIME(18),
	
	/** Bad TKEY mode */
	BAD_MODE(19),
	
	/** Duplicate key name */
	BAD_NAME(20),
	
	/** Algorithm not supported */
	BAD_ALGORITHM(21),
	
	/** Bad truncation */
	BAD_TRUNCATION(22),
	
	/** Bad or missing server cookie */
	BAD_COOKIE(23),
	// 24-3840 unassigned
	//3841-4095 reserved for private use
	//4096-65534 unassigned
	// 65535 rserved, can be allocated by standard
    
    ;


    private static ReverseEnumMap<Byte, ResponseCode> map = new ReverseEnumMap<Byte, ResponseCode>( ResponseCode.class );

    private final byte value;


    private ResponseCode( int value )
    {
        this.value = ( byte ) value;
    }


    public Byte convert()
    {
        return this.value;
    }


    /**
     * Converts an ordinal value into a {@link ResponseCode}.
     *
     * @param value
     * @return The {@link ResponseCode}.
     */
    public static ResponseCode convert( byte value )
    {
        return map.get( value );
    }
}
