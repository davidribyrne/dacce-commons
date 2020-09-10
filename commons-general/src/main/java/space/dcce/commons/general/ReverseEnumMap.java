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

package space.dcce.commons.general;


import java.util.HashMap;
import java.util.Map;



// TODO: Auto-generated Javadoc
/**
 * A map to easily get the actual Enum instance from it's value as seen in the
 * <a href="http://www.javaspecialists.co.za/archive/newsletter.do?issue=113">
 * The JavaSpecialists newsletter</a>.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @param <K> the key type
 * @param <E> the element type
 */
public class ReverseEnumMap<K, E extends Enum<E> & EnumConverter<K>>
{
    
    /** The reverse map. */
    private Map<K, E> reverseMap = new HashMap<K, E>();


    /**
     * Creates a new instance of ReverseEnumMap.
     *
     * @param enumType the enum type
     */
    public ReverseEnumMap( Class<E> enumType )
    {
        for ( E e : enumType.getEnumConstants() )
        {
            reverseMap.put( e.convert(), e );
        }
    }


    /**
     * Return the enum given an ordinal value.
     *
     * @param value the value
     * @return The enum.
     */
    public E get( K value )
    {
        E e = reverseMap.get( value );
        if ( e == null )
        {
            throw new IllegalArgumentException( "Value not found" );
        }
        return e;
    }
}
