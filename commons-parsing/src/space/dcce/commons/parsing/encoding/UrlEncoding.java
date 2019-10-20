package space.dcce.commons.parsing.encoding;

import space.dcce.commons.parsing.AbstractNode;
import space.dcce.commons.parsing.Node;
import space.dcce.commons.parsing.SingleChildNode;
import space.dcce.commons.parsing.collections.MapNode;

public class UrlEncoding extends SingleChildNode implements Node
{

	public UrlEncoding(Node childNode)
	{
		super("URL encoding", childNode);
	}


	@Override
	public byte[] encode()
	{
		return URLEncodingUtils.encodeForURL(getChildNode().encode());
	}



	@Override
	public String getInfoText()
	{
		return new String(encode());
	}



}
