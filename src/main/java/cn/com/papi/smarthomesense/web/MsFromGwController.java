package cn.com.papi.smarthomesense.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.papi.smarthomesense.bean.SenseDeviceSceneRelate;
import cn.com.papi.smarthomesense.bean.SenseDeviceStateLog;
import cn.com.papi.smarthomesense.enums.SenseDeviceType;
import cn.com.papi.smarthomesense.enums.SenseDeviceState;
import cn.com.papi.smarthomesense.service.IRedisUtilService;
import cn.com.papi.smarthomesense.service.ISenseDeviceSceneLinkService;
import cn.com.papi.smarthomesense.service.ISenseDeviceSceneRelateService;
import cn.com.papi.smarthomesense.service.ISenseDeviceStateLogService;
import cn.com.papi.smarthomesense.utils.BaseAction;
import cn.com.papi.smarthomesense.utils.CommonUtils;
import cn.com.papi.smarthomesense.utils.DateUtils;

/**
 * 该类是接收网关返回的消息，并处理
 * @author fanshaowei
 *
 */
@Controller
public class MsFromGwController extends BaseAction{
	@Resource
	ISenseDeviceStateLogService senseDeviceStateLogService;	
	@Resource
	ISenseDeviceSceneLinkService senseDeviceSceneLinkService;
	@Resource
	ISenseDeviceSceneRelateService senseDeviceSceneRelateService;
	@Resource
	IRedisUtilService redisUtilService;
	
	/**
	 * 报警设备状态发生改变时，调用此接口，上报此设备关状态改变消息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping(value = "/gwSendReceipt",method = RequestMethod.POST)
	public void gwSendReceipt( HttpServletRequest request, HttpServletResponse response) throws Exception{
    	//获取网关上报的信息
    	String data = CommonUtils.ReqtoString(request);
		data=data.substring(10);
	    JSONObject jsonData = JSONObject.fromObject(data);
	    
	    String idGateway = jsonData.getString("terminal_code");
	    final String idDevice = jsonData.getString("equipment_code");
	    int type = jsonData.getInt("type");
	    int state = jsonData.getInt("state");
	    int idChannel = jsonData.getInt("number");
	    
	    /******保存设备改变状态的记录******/
	    String stateName = SenseDeviceState.getSenseDeviceState(state);//获取设备上报状态名
	    String deviceType = SenseDeviceType.getDeviceTypeName(CommonUtils.subDeviceTypeCode(idDevice));//获取设备类型	    
	    //记录上报记录
	    SenseDeviceStateLog senseDeviceStateLog = new SenseDeviceStateLog();
	    senseDeviceStateLog.setIdGateway(idGateway);
	    senseDeviceStateLog.setIdDevice(idDevice);
	    senseDeviceStateLog.setStateCode(state);
	    senseDeviceStateLog.setStateName(stateName);
	    senseDeviceStateLog.setDeviceType(deviceType);
	    
	    Date time = DateUtils.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
	    senseDeviceStateLog.setTime(time);
	    senseDeviceStateLogService.add(senseDeviceStateLog);
	    /****************************/
	    	    
	    //查找设备联动情景
	    SenseDeviceSceneRelate senseDeviceSceneRelate = new SenseDeviceSceneRelate();
	    //senseDeviceSceneRelate.setIdGateway(idGateway);
	    senseDeviceSceneRelate.setIdDevice(idDevice);	    
	    List<SenseDeviceSceneRelate> senseDeviceSceneRelateList = 
	    		senseDeviceSceneRelateService.getListByBean(senseDeviceSceneRelate);
	    if(senseDeviceSceneRelateList != null && senseDeviceSceneRelateList.size() > 0){
	    	senseDeviceSceneRelate = senseDeviceSceneRelateList.get(0);
	    	
	    	//执行情景关联控制
		    senseDeviceSceneRelateService.senseDeviceSceneRelateAction(senseDeviceSceneRelate);
	    }	    	    
	    	    	   	  	    
	}
    
    /**
     * 设备注册成功后网关返回的设备注册信息
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping("/gwSendRegisterDoneMsg")
	public void gwSendRegisterDoneMsg( HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		//网关上报注册设备结果
		System.out.println("---------------网关上报注册智能设备结果---------------");
		String data = CommonUtils.ReqtoString(request);
		data=data.substring(10);
		
        JSONObject jsonData = JSONObject.fromObject(data); 	
		int result_code = jsonData.getInt("result_code");
		String idGateway =  jsonData.getString("terminal_code");
		
		System.out.println("注册设备网关：" + jsonData.getInt("result_code") + "结果：" + result_code);
		writeTEXT(CommonUtils.statusBeanToJson(true, "7003", "接收注册设备信息成功", null), response);		
		
		if(ServletHashMap.ASYNC_CONTEXT_REGDEV.containsKey(idGateway)){
			AsyncContext ac = ServletHashMap.ASYNC_CONTEXT_REGDEV.get(idGateway);
			ac.complete();
		}
	}
	
	/**
	 * 设备删除后网关返回的设备删除信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/gwDeleteDeviceMsg")
	public void gwDeleteDeviceMsg(HttpServletRequest request,HttpServletResponse response){
		System.out.println("---------------网关上报设备删除结果--------------------");
		String data = CommonUtils.ReqtoString(request);
		data=data.substring(10);
		
		JSONObject jsonData = JSONObject.fromObject(data); 	
		int result_code = jsonData.getInt("result_code");
		String idGateway =  jsonData.getString("terminal_code");
		System.out.println("删除网关设备：" + jsonData.getInt("result_code") + "结果：" + result_code);
		
		String keyAsyncResp = "del_"+idGateway;
		if(ServletHashMap.ASYNC_CONTEXT_DELETE.containsKey(keyAsyncResp)){
			AsyncContext ac = ServletHashMap.ASYNC_CONTEXT_DELETE.get(keyAsyncResp);
			ac.complete();
		}
	}
		
}
