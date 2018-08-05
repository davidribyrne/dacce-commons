package space.dcce.commons.data_model.encoding;

import space.dcce.commons.data_model.AbstractNode;
import space.dcce.commons.data_model.PrintableNode;
import space.dcce.commons.data_model.SingleChildNode;
import space.dcce.commons.data_model.collections.MapNode;

public class UrlEncoding extends SingleChildNode implements PrintableNode
{

	public UrlEncoding(PrintableNode childNode)
	{
		super(childNode);
	}


	@Override
	public byte[] getBytes()
	{
		return URLEncodingUtils.encodeForURL(getChildNode().getBytes());
	}



}
