package org.apache.directory.server.dns.io;

import org.apache.directory.api.util.Strings;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DnsEncodingUtils
{
	private final static Logger logger = LoggerFactory.getLogger(DnsEncodingUtils.class);



	private DnsEncodingUtils()
	{
	}
	
	

	public static void putDataSize( IoBuffer byteBuffer, int startPosition )
    {
        int endPosition = byteBuffer.position();
        short length = ( short ) ( endPosition - startPosition - 2 );

        byteBuffer.position( startPosition );
        byteBuffer.putShort( length );
        byteBuffer.position( endPosition );
    }


    /**
     * <domain-name> is a domain name represented as a series of labels, and
     * terminated by a label with zero length.
     * 
     * @param byteBuffer the ByteBuffer to encode the domain name into
     * @param domainName the domain name to encode
     */
    public static void putDomainName( IoBuffer byteBuffer, String domainName )
    {
        if ( !Strings.isEmpty( domainName ) )
        {
            String[] labels = domainName.split( "\\." );
        

            for ( String label : labels )
            {
                byteBuffer.put( ( byte ) label.length() );
    
                char[] characters = label.toCharArray();
                
                for ( char c : characters )
                {
                    byteBuffer.put( ( byte ) c );
                }
            }
        }

        byteBuffer.put( ( byte ) 0x00 );
    }






    /**
     * <character-string> is a single length octet followed by that number
     * of characters.  <character-string> is treated as binary information,
     * and can be up to 256 characters in length (including the length octet).
     * 
     * @param byteBuffer The byte buffer to encode the character string into.
     * @param characterString the character string to encode
     */
    public static void putCharacterString( IoBuffer byteBuffer, String characterString )
    {
        byteBuffer.put( ( byte ) characterString.length() );

        char[] characters = characterString.toCharArray();

        for ( int ii = 0; ii < characters.length; ii++ )
        {
            byteBuffer.put( ( byte ) characters[ii] );
        }
    }

}
