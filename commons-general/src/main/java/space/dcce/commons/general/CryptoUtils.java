package space.dcce.commons.general;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class CryptoUtils.
 */
public class CryptoUtils
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(CryptoUtils.class);

	/**
	 * Sha 1.
	 *
	 * @param data the data
	 * @return the byte[]
	 */
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


	/**
	 * Md 5.
	 *
	 * @param data the data
	 * @return the byte[]
	 */
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

	/**
	 * Instantiates a new crypto utils.
	 */
	private CryptoUtils()
	{
	}
}
