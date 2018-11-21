package space.dcce.commons.general;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoUtils
{
	private final static Logger logger = LoggerFactory.getLogger(CryptoUtils.class);

	public static byte[] sha1(byte[] data)
	{
		try
		{
			return MessageDigest.getInstance("SHA").digest(data);
		}
		catch (NoSuchAlgorithmException e)
		{
			logger.error("This never should have happened: " + e.toString());
			throw new UnexpectedException(e); 
		}
	}


	public static byte[] md5(byte[] data)
	{
		try
		{
			return MessageDigest.getInstance("MD5").digest(data);
		}
		catch (NoSuchAlgorithmException e)
		{
			logger.error("This never should have happened: " + e.toString());
			throw new UnexpectedException(e); 
		}
	}

	private CryptoUtils()
	{
	}
}
