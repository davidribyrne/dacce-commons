package space.dcce.commons.parsing.encoding;


import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLEncodingUtils
{


	private URLEncodingUtils()
	{
	}

	static public byte[] decodeURL(byte[] data)
	{
		return URLDecoder.decode(new String(data)).getBytes();
	}


	static public byte[] encodeForURL(byte[] data)
	{
		return URLEncoder.encode(new String(data)).getBytes();
	}
}
