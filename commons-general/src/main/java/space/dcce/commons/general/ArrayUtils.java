package space.dcce.commons.general;

// TODO: Auto-generated Javadoc
/**
 * The Class ArrayUtils.
 */
public class ArrayUtils
{
	
	/**
	 * Join.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the byte[]
	 */
	static public byte[] join(byte[] a, byte[] b)
	{
		int aLen = a.length;
		int bLen = b.length;
		byte[] c = new byte[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}


	/**
	 * Append.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the byte[]
	 */
	static public byte[] append(byte[] a, byte b)
	{
		int aLen = a.length;
		byte[] c = new byte[aLen + 1];
		System.arraycopy(a, 0, c, 0, aLen);
		c[c.length - 1] = b;
		return c;
	}

}
