package cn.com.papi.smarthomesense.service;

import java.util.List;

import cn.com.papi.NettyClient.NettyClient;
import cn.com.papi.smarthomesense.bean.UserBean;

public interface INettyControlDevUtil {

	public void NettySendMsg(NettyClient client,int ctrlType,String to,String data) throws Exception;

	//public String StrConvertData(String idGateway,String idDevice,String type,String number,String state);
	
	//public int getSameFamilyResult(int uid,String idGateway) throws Exception;
	
	//public int getSceneCtrlPower(int uid,int idFamily) throws Exception;
	
	//public int addGatewayToEstate(String estatecode, String housecode,int gid,String username) throws Exception;
	
	//public int deleteDevice(String idDevice,NettyClient client, short CTRL_DEL_DEVICE ,UserBean userBean) throws Exception;

	//public int updateDeviceID(String idDevice,String oldIdDevice,List<ChannelBean> channelBeans) throws Exception;
	
	//public int updateGatewayID(List<GatewayBean> gateways,String idGateway,String oldIdGateway,NettyClient client,short CTRL_DEL_DEVICE,short CTRL_ADD_DEVICE)throws Exception;

	//public int deleteGateway(GatewayBean gateway,String idGateway) throws Exception;
}
