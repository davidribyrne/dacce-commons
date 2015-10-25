package net.dacce.commons.general;

public class ArrayUtils
{
	static public byte[] join(byte[] a, byte[] b)
	{
		int aLen = a.length;
		int bLen = b.length;
		byte[] c = new byte[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}


	static public byte[] append(byte[] a, byte b)
	{
		int aLen = a.length;
		byte[] c = new byte[aLen + 1];
		System.arraycopy(a, 0, c, 0, aLen);
		c[c.length] = b;
		return c;
	}

}
