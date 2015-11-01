package net.dacce.commons.dns.client.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class NoRecordFound extends DnsClientException
{


	public NoRecordFound()
	{
	}


	public NoRecordFound(String message)
	{
		super(message);
	}


}
