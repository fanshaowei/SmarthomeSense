/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cn.com.papi.nettyserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 该nettyServer是用来接收定时服务器的nettyClient发来的定时情景控制信息
 */
public class NettyServer {
    static final int PORT = Integer.parseInt(System.getProperty("port", "12138"));
    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;
    public static ChannelFuture channelFuture;
    
    public static void initNettyServer(String hostServer,int portServer){        
        // Configure the server.
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline pipeline = ch.pipeline(); 
                     
                     pipeline.addLast(new DelimiterBasedFrameDecoder(1024, true, Delimiters.lineDelimiter()));
                     pipeline.addLast(new StringDecoder());
                     
                     pipeline.addLast(new NettyServerHandler());
                 }
             });

			try {
				// Start the server.
				channelFuture = b.bind(hostServer, portServer).sync();
				System.out.println(channelFuture);
				
				// Wait until the server socket is closed.
				channelFuture.channel().closeFuture().sync();
	            
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    public static void closeNettyServer(){
		System.out.println(channelFuture);
		
		bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();    		
    }
    
}
