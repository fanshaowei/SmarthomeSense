package cn.com.papi.NettyClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.ArrayList;
import java.util.UUID;

import com.esms.MessageData;
import com.esms.PostMsg;
import com.esms.common.entity.Account;
import com.esms.common.entity.GsmsResponse;
import com.esms.common.entity.MTPack;
import com.esms.common.entity.MTPack.MsgType;
import com.esms.common.entity.MTPack.SendType;

import cn.com.papi.NettyClient.WebMessage.WebMessageBuilderImpl;
import cn.com.papi.NettyClient.protobuf.WebMessagePojo.WebMessage;

public class nettyClientHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("NettyClient : channelRead.");
	};
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				// 读超时
				System.out.println("client read idle");
			} else if (event.state() == IdleState.WRITER_IDLE) {
				// 写超时
				System.out.println("client write idle");
				sendHeartBeat(ctx,evt);
			} else if (event.state() == IdleState.ALL_IDLE) {
				// 读写超时
//				System.out.println("client rw idle");
			}
		}
	}
	
	private void sendHeartBeat(ChannelHandlerContext ctx, Object evt) {
		// 发送心跳包给server
		short CTRL_HEARTBEAT = 9999;
		String to = "NettyServer"; 
		String data = "";
		WebMessageBuilderImpl impl = new WebMessageBuilderImpl();
		WebMessage msg = impl.WebMessageBuild(CTRL_HEARTBEAT, to, data);
		
		ctx.channel().writeAndFlush(msg);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		System.out.println("channelActive");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		System.out.println("channelInactive!");
		Account ac = new Account("ajb2@ajb", "YU12@#ad");
		PostMsg pm = new PostMsg();
		pm.getCmHost().setHost("211.147.239.62", 8450);//设置网关的IP和port，用于发送信息
		MTPack pack = new MTPack();
		pack.setBatchID(UUID.randomUUID());
		pack.setBatchName("2131");
		pack.setMsgType(MsgType.SMS);
		pack.setBizType(1);
		pack.setDistinctFlag(false);
		
		ArrayList<MessageData> msgs = new ArrayList<MessageData>();
		
		/** 群发，多号码一内容 */
		pack.setSendType(SendType.MASS);
		String content = "长连接断开";
		MessageData messageData1 = new MessageData("13428283624", content); 
		msgs.add(messageData1);
		pack.setMsgs(msgs);
	
		try {
			GsmsResponse resp;
			resp = pm.post(ac, pack);
			System.out.println(resp);
			System.out.println("发送成功");
		} catch (Exception e) {
			System.out.println("发送失败");
			e.printStackTrace();
		}
		NettyClient.reConnect();
	}
}

