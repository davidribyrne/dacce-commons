package space.dcce.commons.dns.io;

import org.apache.mina.core.buffer.IoBuffer;

// TODO: Auto-generated Javadoc
/**
 * The Class DnsDecodingUtils.
 */
public class DnsDecodingUtils
{

	/**
	 * Instantiates a new dns decoding utils.
	 */
	private DnsDecodingUtils()
	{
	}
	
	/**
	 * Gets the character string.
	 *
	 * @param byteBuffer the byte buffer
	 * @param length the length
	 * @return the character string
	 */
	public static String getCharacterString(IoBuffer byteBuffer, int length)
	{
		StringBuilder sb = new StringBuilder(length);
		int count = 0;
		do
		{
			int subLength = byteBuffer.get() & 0xFF;
			count += subLength + 1;
	        for ( int ii = 0; ii < subLength; ii++ )
	        {
	        	sb.append((char)byteBuffer.get());
	        }
		} while (count < length);
        return sb.toString();
	}

    /**
     * Gets the domain name.
     *
     * @param byteBuffer the byte buffer
     * @return the domain name
     */
    public static String getDomainName( IoBuffer byteBuffer )
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
}
