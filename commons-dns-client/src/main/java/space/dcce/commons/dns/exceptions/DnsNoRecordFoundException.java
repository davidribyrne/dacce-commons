package space.dcce.commons.dns.exceptions;


public class DnsNoRecordFoundException extends DnsClientException
{
	private static final long serialVersionUID = 1L;


	public DnsNoRecordFoundException()
	{
	}


	public DnsNoRecordFoundException(String message)
	{
		super(message);
	}


}
