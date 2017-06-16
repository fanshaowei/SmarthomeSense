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
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 该nettyServer是用来接收定时服务器的nettyClient发来的定时情景控制信息
 */
public class NettyServer {
	private static Logger logger = Logger.getLogger(NettyServer.class.getName());
	
    static final int PORT = Integer.parseInt(System.getProperty("port", "12138"));
    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;
    public static ChannelFuture channelFuture;
    private final static AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger();
    
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
                     pipeline.addLast(new IdleStateHandler(90, 0, 0, TimeUnit.SECONDS));
                     pipeline.addLast(idleStateTrigger);
                     pipeline.addLast(new DelimiterBasedFrameDecoder(2048, true, Delimiters.lineDelimiter()));
                     pipeline.addLast(new StringDecoder());
                     
                     pipeline.addLast(new NettyServerHandler());
                 }
             });
			
			// Start the server.
			channelFuture = b.bind(hostServer, portServer).sync();
				
			// Wait until the server socket is closed.
			channelFuture.channel().closeFuture().sync();
	        
        }catch(Exception e){
        	e.printStackTrace();
        	logger.info("connects to  fails"); 
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
