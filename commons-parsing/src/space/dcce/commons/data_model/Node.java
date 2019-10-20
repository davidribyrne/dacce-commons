package space.dcce.commons.data_model;

public interface Node
{
	public NodeType getNodeType();
	public Node[] getChildren();
//	public byte[] encode();
	public String getInfoText();
	public String getName();
}
