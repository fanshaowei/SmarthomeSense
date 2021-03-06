package cn.com.papi.nettyserver;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter{
	private final Logger logger = Logger.getLogger(AcceptorIdleStateTrigger.class);
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		
		if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
            	logger.info("SmarthomeSense netty server read idle");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
	}
}
