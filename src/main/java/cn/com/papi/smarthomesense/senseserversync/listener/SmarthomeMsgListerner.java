package cn.com.papi.smarthomesense.senseserversync.listener;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import cn.com.papi.smarthomesense.bean.SenseDeviceBean;
import cn.com.papi.smarthomesense.senseserversync.bean.SmarthomeSyncDataBean;
import cn.com.papi.smarthomesense.service.ISenseDeviceService;

/**
 * 该监听器为redis消息订阅通道，用来接收安居家园Smarthome服务器删除家庭、网关 的信息
 * 订阅通道名为 SmarthomeDataSync
 * @author fanshaowei
 *
 */
@Component("messageListener")
public class SmarthomeMsgListerner implements MessageListener{
	@Resource
	RedisTemplate<String,Object> redisTemplate;	
	@Resource
	ISenseDeviceService senseDeviceService;
	
	@Override
	public void onMessage(Message message, byte[] pattern) {		
		String msgStr = (String) redisTemplate.getValueSerializer().deserialize(message.getBody());
		
		SmarthomeSyncDataBean msgJson = (SmarthomeSyncDataBean) JSONObject.toBean(JSONObject.fromObject(msgStr), SmarthomeSyncDataBean.class);		
		String msgType = msgJson.getMsgType();
				
		switch (msgType) {
		case "del-gateway":
			String gatewayId = msgJson.getMsgMap().get("gatewayId").toString();
			
			try {
				List<SenseDeviceBean> deviceList = senseDeviceService.getListByIdGateway(gatewayId);
				if(deviceList != null){
					senseDeviceService.deleteByIdGateway(gatewayId);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		case "del-family":	
			int familyId = Integer.parseInt(msgJson.getMsgMap().get("familyId").toString());
			try {
				List<SenseDeviceBean> deviceList = senseDeviceService.getListByIdFamily(familyId);
				if(deviceList != null){
					senseDeviceService.deleteByIdFamily(familyId);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		case "update-gatewayId":
			@SuppressWarnings("unchecked")
			Map<String,Object> paramsMap = (Map<String, Object>) msgJson.getMsgMap();
			String oldGatewayId = paramsMap.get("oldIdGateway").toString();
			
			try {
				List<SenseDeviceBean> deviceList = senseDeviceService.getListByIdGateway(oldGatewayId);
				if(deviceList != null){
					senseDeviceService.updateSenseDeviceGatewayId(paramsMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
			break;			
		default:
			break;
		}
	}

}
