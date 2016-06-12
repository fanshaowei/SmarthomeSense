package cn.com.papi.NettyClient.WebMessage;

import cn.com.papi.NettyClient.protobuf.WebMessagePojo.WebMessage;

public interface WebMessageBuilder {
	public static final short MSG_PUSH = 0;
	public static final short MSG_TRANSMIT = 1;
	
	/*
	 * 云服务器到家电网关
		2580	云服务器发送单控命令到家电网关
		2581	家电网关返回2580状态
		2582	云服务器发送情景控制命令到家电网关
		2583	家电网关返回2582状态
		2584	云服务器更改家电网关设置信息(用户密码等信息)
		2585	家电网关返回2584状态
		
		5806	心跳ping,云服务器回复家电网关
	 */
	public static final short CTRL_SINGLE = 2580;
	public static final short CTRL_SINGLE_ACK = 2581;
	public static final short CTRL_SCENE = 2582;
	public static final short CTRL_SCENE_ACK = 2583;
	public static final short MODIFY_GW = 2584;
	public static final short MODIFY_GW_ACK = 2585;
	
	
	public WebMessage WebMessageBuild(int ctrlType,String to,String data);
	
}
