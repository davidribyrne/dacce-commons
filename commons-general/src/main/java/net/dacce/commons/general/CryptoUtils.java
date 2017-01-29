package net.dacce.commons.general;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

	private CryptoUtils()
	{
	}
}
