package space.dcce.commons.data_model.encoding;

import java.util.*;

import space.dcce.commons.data_model.Value;

public interface Encoding
{
	public String geEncodingtName();
	public Value getValue();
	public void setValue(Value value);
	public byte[] getEncodedValue();
	
}
