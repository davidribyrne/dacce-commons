//package mcp.commons.netaddr;
//
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
//import space.dcce.commons.netaddr.IP4Utils;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
//
//public class IP4UtilsTest
//{
//
//	@Rule
//	public ExpectedException exception = ExpectedException.none();
//
//
//	@Test
//	public void testStringOctetsToDecimal1()
//	{
//		assertEquals(0, IP4Utils.stringToDecimal("0.0.0.0"));
//		assertEquals(2130706433, IP4Utils.stringToDecimal("127.0.0.1"));
//	}
//
//
//	@Test
//	public void testStringOctetsToDecimal2()
//	{
//		exception.expect(IllegalArgumentException.class);
//		IP4Utils.stringToDecimal("asdf");
//	}
//
//
//	@Test
//	public void testStringOctetsToDecimal3()
//	{
//		exception.expect(IllegalArgumentException.class);
//		IP4Utils.stringToDecimal("-1");
//	}
//
//
//	@Test
//	public void testStringOctetsToDecimal4()
//	{
//		exception.expect(IllegalArgumentException.class);
//		IP4Utils.stringToDecimal("256.0.0.0");
//	}
//
//
//	@Test
//	public void testStringOctetsToDecimal5()
//	{
//		exception.expect(IllegalArgumentException.class);
//		IP4Utils.stringToDecimal("1.2.3.4.5");
//	}
//
//
//	@Test
//	public void testStringOctetsToDecimal6()
//	{
//		exception.expect(IllegalArgumentException.class);
//		IP4Utils.stringToDecimal("1-2.3.4.5");
//	}
//
//
//	@Test
//	public void testStringOctetsToDecimal7()
//	{
//		exception.expect(IllegalArgumentException.class);
//		IP4Utils.stringToDecimal("1.2.3.4/1");
//	}
//
//
//	@Test
//	public void testBytesOctetsToDecimal()
//	{
//
//	}
//
//
//	@Test
//	public void testDecimalToOctets()
//	{
//		assertArrayEquals("127.0.0.1", new byte[] { 0x7f, 0x00, 0x00, 0x01 }, IP4Utils.decimalToOctets(2130706433));
//		assertArrayEquals("0.0.0.0", new byte[] { 0x00, 0x00, 0x00, 0x00 }, IP4Utils.decimalToOctets(0));
//		assertArrayEquals("0.0.0.1", new byte[] { 0x00, 0x00, 0x00, 0x01 }, IP4Utils.decimalToOctets(1));
//		assertArrayEquals("255.255.255.255", new byte[] { -1, -1, -1, -1 }, IP4Utils.decimalToOctets(-1));
//	}
//
//
//}
