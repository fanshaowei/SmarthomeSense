package cn.com.papi.NettyClient;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import cn.com.papi.NettyClient.protobuf.WebMessagePojo;

public class ChannelInit extends ChannelInitializer<SocketChannel> {

	private static int WRITE_IDLE_TIMEOUT = 100000;//30000;
	private static int READ_IDLE_TIMEOUT = 0;
	private static int ALL_IDLE_TIMEOUT = 0;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		// 获取过滤器链路
		ChannelPipeline pipeline = ch.pipeline();

		// idle时间处理器，完成心跳处理
		pipeline.addLast("heartBeatHandler", new IdleStateHandler(
				READ_IDLE_TIMEOUT, WRITE_IDLE_TIMEOUT, ALL_IDLE_TIMEOUT,
				TimeUnit.MILLISECONDS));

		// 添加protobuf解码处理器，半包处理+protobuf框架解码器
		pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		// 添加web message解码器
		pipeline.addLast("protobufDecoder", new ProtobufDecoder(
				WebMessagePojo.WebMessage.getDefaultInstance()));

		// 添加protobuf编码处理器，半包+protobuf框架编码器
		pipeline.addLast("frameEncoder",
				new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast("protobufEncoder", new ProtobufEncoder());

		pipeline.addLast("nettyClientHandler",new nettyClientHandler());

	}

}
