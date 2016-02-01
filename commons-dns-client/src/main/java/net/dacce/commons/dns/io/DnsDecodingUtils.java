package net.dacce.commons.dns.io;

import org.apache.mina.core.buffer.IoBuffer;

public class DnsDecodingUtils
{

	private DnsDecodingUtils()
	{
	}
	
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

    public static String getDomainName( IoBuffer byteBuffer )
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
}
