package space.dcce.commons.data_model.encoding;

import java.util.Base64;

import space.dcce.commons.data_model.AbstractNode;
import space.dcce.commons.data_model.Node;
import space.dcce.commons.data_model.SingleChildNode;


public class Base64Encoder extends SingleChildNode
{
	private Mode base64Mode = Mode.BASIC;


	public Base64Encoder(Node childNode)
	{
		super("Base64 encoding", childNode);
	}



	@Override
	public byte[] encode()
	{
		switch (base64Mode)
		{
			case BASIC:
				return Base64.getEncoder().encode(getChildNode().encode());
			case MIME:
				return Base64.getMimeEncoder().encode(getChildNode().encode());
			case URL:
				return Base64.getUrlEncoder().encode(getChildNode().encode());
		}
		throw new IllegalStateException("Unknown base64 mode");
	}


	public static enum Mode
	{
		BASIC, MIME, URL;
	}


	/**
	 * @return the base64Mode
	 */
	public Mode getBase64Mode()
	{
		return base64Mode;
	}


	/**
	 * @param base64Mode
	 *            the base64Mode to set
	 */
	public void setBase64Mode(Mode base64Mode)
	{
		this.base64Mode = base64Mode;
	}



	@Override
	public String getInfoText()
	{
		return new String(encode());
	}


}

