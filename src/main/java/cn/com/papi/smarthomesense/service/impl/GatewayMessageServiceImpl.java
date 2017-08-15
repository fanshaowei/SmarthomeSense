package cn.com.papi.smarthomesense.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import cn.com.papi.smarthomesense.bean.SenseDeviceSceneRelate;
import cn.com.papi.smarthomesense.bean.SenseDeviceStateLog;
import cn.com.papi.smarthomesense.bean.gatewayresp.GatewayRespBasic;
import cn.com.papi.smarthomesense.bean.gatewayresp.GatewayRespCombustibleGasSensor;
import cn.com.papi.smarthomesense.bean.gatewayresp.GatewayRespDoorMagnetic;
import cn.com.papi.smarthomesense.bean.gatewayresp.GatewayRespInfraredSensor;
import cn.com.papi.smarthomesense.bean.gatewayresp.GatewayRespTempHumiditySensor;
import cn.com.papi.smarthomesense.enums.SenseDeviceState;
import cn.com.papi.smarthomesense.enums.SenseDeviceType;
import cn.com.papi.smarthomesense.service.IGatewayMessageService;
import cn.com.papi.smarthomesense.service.ISenseDeviceSceneRelateService;
import cn.com.papi.smarthomesense.service.ISenseDeviceStateLogService;
import cn.com.papi.smarthomesense.utils.CommonUtils;
import cn.com.papi.smarthomesense.utils.DateUtils;

@Service("gatewayMessageService")
public class GatewayMessageServiceImpl implements IGatewayMessageService {
    
	@Resource
	ISenseDeviceSceneRelateService senseDeviceSceneRelateService;
	@Resource
	ISenseDeviceStateLogService senseDeviceStateLogService;
	
	@Override
	public void parseSenseDeviceAndAction(JSONObject senseDeviceJson){
	    String idGateway = senseDeviceJson.getString("terminal_code");
	    String idDevice = senseDeviceJson.getString("equipment_code");
	    String deviceTypeCode = CommonUtils.subDeviceTypeCode(idDevice);	 
	    
	    SenseDeviceSceneRelate senseDeviceSceneRelate = new SenseDeviceSceneRelate();
	    senseDeviceSceneRelate.setIdGateway(idGateway);
	    senseDeviceSceneRelate.setIdDevice(idDevice);
	    
	    switch (deviceTypeCode) {
		case "8211"://门磁
			GatewayRespDoorMagnetic gatewayRespDoorMagnetic = 
				(GatewayRespDoorMagnetic)JSONObject.toBean(senseDeviceJson, GatewayRespDoorMagnetic.class);
			
			doorMagneticAction(gatewayRespDoorMagnetic, senseDeviceSceneRelate);
			recordSenseDeviceLog(gatewayRespDoorMagnetic);					 
		    
			break;	
			
		case "0521":case "4521"://温湿度
			GatewayRespTempHumiditySensor gatewayRespTempHumiditySensor = 
				(GatewayRespTempHumiditySensor)JSONObject.toBean(senseDeviceJson, GatewayRespTempHumiditySensor.class);
			
			tempHumiditySensorAction(gatewayRespTempHumiditySensor, senseDeviceSceneRelate);
			recordSenseDeviceLog(gatewayRespTempHumiditySensor);	
			
			break;
		
		case "0531":case "4531"	://可燃气体
			GatewayRespCombustibleGasSensor gatewayRespCombustibleGasSensor = 
				(GatewayRespCombustibleGasSensor)JSONObject.toBean(senseDeviceJson, GatewayRespCombustibleGasSensor.class);
			
			combustibleGasSensorAction(gatewayRespCombustibleGasSensor, senseDeviceSceneRelate);
			recordSenseDeviceLog(gatewayRespCombustibleGasSensor);
			
			break;
		
		case "0511"://人体红外
			GatewayRespInfraredSensor gatewayRespInfraredSensor = 
				(GatewayRespInfraredSensor)JSONObject.toBean(senseDeviceJson, GatewayRespInfraredSensor.class);
			
			infraredSensorAction(gatewayRespInfraredSensor, senseDeviceSceneRelate);
			recordSenseDeviceLog(gatewayRespInfraredSensor);
			
			break;
			
		default:
			break;
		}
  
	}
	
	/**
	 * 门磁设备关联触发条件
	 * @param doorMagnetic
	 * @param senseDeviceSceneRelate
	 */
	public void doorMagneticAction(GatewayRespDoorMagnetic doorMagnetic, SenseDeviceSceneRelate senseDeviceSceneRelate){
		SenseDeviceSceneRelate senseDeviceSceneRelateTemp = senseDeviceSceneRelate;
		
		if(doorMagnetic.getState() == 1){//1普通报警状态,触发关联任务	
	    	//查找设备联动情景		    		    
		    List<SenseDeviceSceneRelate> senseDeviceSceneRelateList = senseDeviceSceneRelateService.getListByBean(senseDeviceSceneRelateTemp);
		    if(senseDeviceSceneRelateList != null && senseDeviceSceneRelateList.size() > 0){
		    	for(int i = 0; i<senseDeviceSceneRelateList.size(); i++){
		    		senseDeviceSceneRelateTemp = senseDeviceSceneRelateList.get(i);
		    		senseDeviceSceneRelateService.senseDeviceSceneRelateAction(senseDeviceSceneRelateTemp);//执行情景关联控制
		    	}			    				    				    					    
		    }
	    }
	}
	
	/**
	 * 温湿度感应器触发条件
	 * @param tempHumiditySensor
	 * @param senseDeviceSceneRelate
	 */
	public void tempHumiditySensorAction(GatewayRespTempHumiditySensor tempHumiditySensor, SenseDeviceSceneRelate senseDeviceSceneRelate){
		SenseDeviceSceneRelate senseDeviceSceneRelateTemp = senseDeviceSceneRelate;		
		
		JSONObject triggerSourceJson = new JSONObject();
		float humidity = 0;
		float temperature = 0;
		List<SenseDeviceSceneRelate> senseDeviceSceneRelateList = senseDeviceSceneRelateService.getListByBean(senseDeviceSceneRelateTemp);
		if(senseDeviceSceneRelateList != null && senseDeviceSceneRelateList.size() > 0){
	    	for(int i = 0; i<senseDeviceSceneRelateList.size(); i++){
	    		senseDeviceSceneRelateTemp = senseDeviceSceneRelateList.get(i);
	    		
	    		triggerSourceJson = JSONObject.fromObject(senseDeviceSceneRelateTemp.getTriggerSourceJson());
	    		humidity = Float.parseFloat(triggerSourceJson.getString("humidity"));
	    		temperature = Float.parseFloat(triggerSourceJson.getString("temperature"));
	    		
	    		if(tempHumiditySensor.getHumidity() >= humidity && tempHumiditySensor.getTemperature() >= temperature){
	    			senseDeviceSceneRelateService.senseDeviceSceneRelateAction(senseDeviceSceneRelateTemp);//执行情景关联控制
	    		}	    			    	
	    	}			    				    				    					    
	    }
	}
	
	/**
	 * 可燃气体感应器触发条件
	 * @param combustibleGasSensor
	 * @param senseDeviceSceneRelate
	 */
	public void combustibleGasSensorAction(GatewayRespCombustibleGasSensor combustibleGasSensor , SenseDeviceSceneRelate senseDeviceSceneRelate){
		SenseDeviceSceneRelate senseDeviceSceneRelateTemp = senseDeviceSceneRelate;		
		
		JSONObject triggerSourceJson = new JSONObject();
		float carbon_monoxide = 0;
		float methane = 0;
		List<SenseDeviceSceneRelate> senseDeviceSceneRelateList = senseDeviceSceneRelateService.getListByBean(senseDeviceSceneRelateTemp);
		if(senseDeviceSceneRelateList != null && senseDeviceSceneRelateList.size() > 0){
	    	for(int i = 0; i<senseDeviceSceneRelateList.size(); i++){
	    		senseDeviceSceneRelateTemp = senseDeviceSceneRelateList.get(i);
	    		
	    		triggerSourceJson = JSONObject.fromObject(senseDeviceSceneRelateTemp.getTriggerSourceJson());
	    		carbon_monoxide = Float.parseFloat(triggerSourceJson.getString("carbon_monoxide"));
	    		methane = Float.parseFloat(triggerSourceJson.getString("methane"));
	    		
	    		if(combustibleGasSensor.getCarbon_monoxide() >= carbon_monoxide && combustibleGasSensor.getMethane() >= methane){
	    			senseDeviceSceneRelateService.senseDeviceSceneRelateAction(senseDeviceSceneRelateTemp);//执行情景关联控制
	    		}	    			    	
	    	}			    				    				    					    
	    }
	}
	
	/**
	 * 人体红外感应器
	 * @param infraredSensor
	 * @param senseDeviceSceneRelate
	 */
	public void infraredSensorAction(GatewayRespInfraredSensor infraredSensor , SenseDeviceSceneRelate senseDeviceSceneRelate){
		SenseDeviceSceneRelate senseDeviceSceneRelateTemp = senseDeviceSceneRelate;		
		
		JSONObject triggerSourceJson = new JSONObject();
		int illuminance = 0;
		List<SenseDeviceSceneRelate> senseDeviceSceneRelateList = senseDeviceSceneRelateService.getListByBean(senseDeviceSceneRelateTemp);
		if(senseDeviceSceneRelateList != null && senseDeviceSceneRelateList.size() > 0){
	    	for(int i = 0; i<senseDeviceSceneRelateList.size(); i++){
	    		senseDeviceSceneRelateTemp = senseDeviceSceneRelateList.get(i);
	    		
	    		triggerSourceJson = JSONObject.fromObject(senseDeviceSceneRelateTemp.getTriggerSourceJson());
	    		illuminance = triggerSourceJson.getInt("illuminance");
	    		
	    		if(infraredSensor.getIlluminance() >= illuminance && infraredSensor.getState() == 1){
	    			senseDeviceSceneRelateService.senseDeviceSceneRelateAction(senseDeviceSceneRelateTemp);//执行情景关联控制
	    		}	    			    	
	    	}			    				    				    					    
	    }
	}
	
	
	/**
	 * 记录上报日志
	 * @param obj
	 */
	public void recordSenseDeviceLog(Object obj) {
		SenseDeviceStateLog senseDeviceStateLog = new SenseDeviceStateLog();
		
		Date time = DateUtils.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
		String idGateway = ((GatewayRespBasic)obj).getTerminal_code();
	    String idDevice = ((GatewayRespBasic)obj).getEquipment_code();
		String deviceTypeCode = CommonUtils.subDeviceTypeCode(idDevice);
	    String deviceType = SenseDeviceType.getDeviceTypeName(deviceTypeCode);//获取设备类型
	    
	    senseDeviceStateLog.setIdGateway(idGateway);
	    senseDeviceStateLog.setIdDevice(idDevice);
	    senseDeviceStateLog.setDeviceType(deviceType);	    
	    senseDeviceStateLog.setTime(time);			
		
		Map<String,Object> logMsgMap = new HashMap<String,Object>();		
		String fieldName = null;
		String fieldValue = null;
		Method method = null;
		Field[] classProperties = obj.getClass().getDeclaredFields();	
		try{
			for(Field field : classProperties){
				fieldName = field.getName();
				method = obj.getClass().getMethod("get" + CommonUtils.toUpperCase4Index(fieldName));
				fieldValue = method.invoke(obj).toString();
				
				if("state".equals(fieldName)){
					String stateName = SenseDeviceState.getSenseDeviceState(Integer.parseInt(fieldValue));
					logMsgMap.put("stateName", stateName);
				}
				
				logMsgMap.put(fieldName, fieldValue);				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		senseDeviceStateLog.setMsg(JSONObject.fromObject(logMsgMap).toString());		    
	    try {
			senseDeviceStateLogService.add(senseDeviceStateLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
