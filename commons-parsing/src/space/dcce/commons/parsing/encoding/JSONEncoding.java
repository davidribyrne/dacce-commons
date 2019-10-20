package space.dcce.commons.parsing.encoding;

import java.io.IOException;

import space.dcce.commons.parsing.AbstractNode;
import space.dcce.commons.parsing.NodeType;
import space.dcce.commons.parsing.PrintableNode;
import space.dcce.commons.parsing.collections.ObjectNode;


public class JSONEncoding extends AbstractNode implements PrintableNode
{
	private ObjectNode childNode;

	
	public ObjectNode getChildNode()
	{
		return childNode;
	}


	public void setChildNode(ObjectNode child)
	{
		this.childNode = child;
	}


	public JSONEncoding(ObjectNode childNode)
	{
		super(NodeType.ENCODING);
		this.childNode = childNode;
	}


	@Override
	public byte[] getBytes()
	{
		return getEncodedBytes();
	}


	public byte[] getEncodedBytes()
	{
		return JSONUtils.encode(this);
	}

}
