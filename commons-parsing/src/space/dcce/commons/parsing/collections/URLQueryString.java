package space.dcce.commons.parsing.collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.dcce.commons.parsing.Node;
import space.dcce.commons.parsing.PrintableNode;
import space.dcce.commons.parsing.encoding.UrlEncoding;
import space.dcce.commons.parsing.primitives.StringPrimitive;

import java.util.*;

public class URLQueryString extends MapNode<UrlEncoding, UrlEncoding> implements PrintableNode
{


	public URLQueryString()
	{
		
	}

	@Override
	public byte[] getBytes()
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(KeyValuePair<UrlEncoding, UrlEncoding> pair: getPairs())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append("&");
			}
			sb.append(new String(pair.getKey().getBytes()));
			sb.append("=");
			sb.append(new String(pair.getValue().getBytes()));
		}
		return sb.toString().getBytes();
	}
}
