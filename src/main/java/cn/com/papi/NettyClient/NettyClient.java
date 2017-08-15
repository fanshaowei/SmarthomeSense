package cn.com.papi.NettyClient;

import cn.com.papi.NettyClient.protobuf.WebMessagePojo.WebMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


public class NettyClient {

	// 客户端线程组
	private static Bootstrap bootstrap = null;
	private static EventLoopGroup eventLoopGroup = null;
	private static ChannelFuture cf = null;
	// 服务器ip、端口
	private static String serverIp = null;
	private static String serverPort = null;

	public String getServerIp() {
		return serverIp;
	}

	/*public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}*/

	public String getServerPort() {
		return serverPort;
	}

	/*public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}*/

	public NettyClient() {
	}

	public NettyClient(String ip, String port) {
		serverIp = ip;
		serverPort = port;
	}

	//检测是否断开连接
	public static void reConnect() {
		try {
			disconnect();
		} catch (InterruptedException e) {
			System.out.println("已经释放资源或者释放资源失败");
		}
		connect();	   //重新连接	,会阻塞在这里
//		Thread thread = new Thread(){
//			@Override
//			public void run(){
//				while(true){
//					System.out.println("try to connect again!!!");
//					super.run();
//					connect();	   //重新连接	,会阻塞在这里
//					System.out.println("try to connect again fail!!!");
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						System.out.println("sleep fail");
//					}
//				}
//				
//			}
//			
//		};
//		thread.setDaemon(true);
//		thread.run();
	}
	
	/*
	 * 连接服务器
	 */
	public static void connect() {
		// 客户端线程组
		bootstrap = new Bootstrap();
		eventLoopGroup = new NioEventLoopGroup();

		try {
			bootstrap.group(eventLoopGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			// 绑定事件处理器
			bootstrap.handler(new ChannelInit());
			System.out.println("IP:" + serverIp +"---PORT:" + serverPort);
			// client连接到server端口，阻塞到连接成功为止
			cf = bootstrap.connect(serverIp, Integer.parseInt(serverPort))
					.sync();

			System.out.println("nettyClient started!");
			// cf.channel().writeAndFlush("").addListener(ChannelFutureListener.CLOSE);//短连接
			cf.channel().closeFuture().sync();// 成功后会一直阻塞在这里。下一行不会被执行。

		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			eventLoopGroup.shutdownGracefully();
		}
	}
	
	/*
	 * 退出客户端
	 */
	public static void disconnect() throws InterruptedException {
		eventLoopGroup.shutdownGracefully();
	}

	//发送函数
	public int msgWrite(WebMessage msgBuilder) {

		if(cf.channel().isActive()){
		cf.channel().writeAndFlush(msgBuilder); //发送数据
		System.out.println("msgWrite done!!!");
		return 0;
		}else{
			System.out.println("cf.channel().is not Active!!!!");
			System.out.println("try to connect again!!!!");
			connect();	   //重新连接
			cf.channel().writeAndFlush(msgBuilder); //发送数据
			return 0;
		}
	}
}
