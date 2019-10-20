package space.dcce.commons.data_model.encoding;

import space.dcce.commons.data_model.AbstractNode;
import space.dcce.commons.data_model.Node;
import space.dcce.commons.data_model.SingleChildNode;
import space.dcce.commons.data_model.collections.MapNode;

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
