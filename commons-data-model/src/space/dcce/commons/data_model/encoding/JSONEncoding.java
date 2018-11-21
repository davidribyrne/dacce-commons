package space.dcce.commons.data_model.encoding;

import java.io.IOException;

import space.dcce.commons.data_model.AbstractNode;
import space.dcce.commons.data_model.NodeType;
import space.dcce.commons.data_model.PrintableNode;
import space.dcce.commons.data_model.collections.ObjectNode;


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
