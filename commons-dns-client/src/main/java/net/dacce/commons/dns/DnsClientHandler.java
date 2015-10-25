package net.dacce.commons.dns;

import org.apache.directory.server.dns.messages.DnsMessage;
import org.apache.directory.server.dns.protocol.DnsProtocolTcpCodecFactory;
import org.apache.directory.server.dns.protocol.DnsProtocolUdpCodecFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DnsClientHandler extends IoHandlerAdapter
{
	private final static Logger logger = LoggerFactory.getLogger(DnsClientHandler.class);


	public DnsClientHandler()
	{
	}

	@Override
    public void sessionCreated( IoSession session ) throws Exception
    {
        if ( session.getTransportMetadata().isConnectionless() )
        {
            session.getFilterChain().addFirst( "codec",
                new ProtocolCodecFilter( DnsProtocolUdpCodecFactory.getInstance() ) );
        }
        else
        {
            session.getFilterChain().addFirst( "codec",
                new ProtocolCodecFilter( DnsProtocolTcpCodecFactory.getInstance() ) );
        }
    }

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{
		logger.debug( "{} RCVD:  {}", session.getRemoteAddress(), message );
		DnsMessage response = (DnsMessage) message;
		
	}



}
