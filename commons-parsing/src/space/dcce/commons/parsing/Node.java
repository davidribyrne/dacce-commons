package space.dcce.commons.parsing;

public interface Node
{
	public NodeType getNodeType();
	public Node[] getChildren();
//	public byte[] encode();
	public String getInfoText();
	public String getName();
}
