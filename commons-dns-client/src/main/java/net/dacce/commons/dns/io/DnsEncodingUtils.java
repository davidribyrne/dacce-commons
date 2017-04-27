package net.dacce.commons.dns.io;

import org.apache.mina.core.buffer.IoBuffer;

import net.dacce.commons.general.StringUtils;

public class DnsEncodingUtils
{

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
        if ( !StringUtils.isEmptyOrNull(domainName ) )
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
     * 
     * @param byteBuffer The byte buffer to encode the character string into.
     * @param characterString the character string to encode
     */
    public static void putCharacterString( IoBuffer byteBuffer, String characterString )
    {
    	for (int i = 0; i < characterString.length(); i+= 256)
		{
    		int end = Math.min(characterString.length(), i + 255);
			String chunk = characterString.substring(i, end);
	    	byteBuffer.put( ( byte ) chunk.length() );
	        char[] characters = chunk.toCharArray();

	        for ( int ii = 0; ii < characters.length; ii++ )
	        {
	            byteBuffer.put( ( byte ) characters[ii] );
	        }
			
		}
    }

}
