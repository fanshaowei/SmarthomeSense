package cn.com.papi.smarthomesense.service;

import cn.com.papi.NettyClient.NettyClient;

public interface INettyControlDevUtil {

	public void NettySendMsg(NettyClient client,int ctrlType,String to,String data) throws Exception;
}
