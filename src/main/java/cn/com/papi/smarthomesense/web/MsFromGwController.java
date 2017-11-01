package cn.com.papi.smarthomesense.web;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.papi.smarthomesense.service.IGatewayMessageService;
import cn.com.papi.smarthomesense.service.IRedisUtilService;
import cn.com.papi.smarthomesense.service.ISenseDeviceSceneRelateService;
import cn.com.papi.smarthomesense.service.ISenseDeviceStateLogService;
import cn.com.papi.smarthomesense.utils.BaseAction;
import cn.com.papi.smarthomesense.utils.CommonUtils;

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
	ISenseDeviceSceneRelateService senseDeviceSceneRelateService;
	@Resource
	IRedisUtilService redisUtilService;
	@Resource
    IGatewayMessageService gatewayMessageService;
	
	/**
	 * 报警设备状态发生改变时，调用此接口，上报此设备关状态改变消息,并执行相关的关联情景
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping(value = "/gwSendReceipt",method = RequestMethod.POST)
	public void gwSendReceipt( HttpServletRequest request, HttpServletResponse response) throws Exception{    	
    	String data = CommonUtils.ReqtoString(request).substring(10);//获取网关上报的信息
	    
    	JSONObject jsonData = JSONObject.fromObject(data);
    	System.out.println(jsonData.toString());
    	gatewayMessageService.parseSenseDeviceAndAction(jsonData);        
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
		
		System.out.println("注册设备网关：" + jsonData.getInt("result_code") + ",结果：" + result_code);
		writeTEXT(CommonUtils.statusBeanToJson(true, "7003", "接收网关注册设备信息成功", null), response);		
		
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
	@RequestMapping("/gwSendDelDoneMsg")
	public void gwSendDelDoneMsg(HttpServletRequest request,HttpServletResponse response){
		System.out.println("---------------网关上报设备删除结果--------------------");
		String data = CommonUtils.ReqtoString(request);
		data=data.substring(10);
		
		JSONObject jsonData = JSONObject.fromObject(data); 	
		int result_code = jsonData.getInt("result_code");
		String idGateway =  jsonData.getString("terminal_code");
		System.out.println("删除网关设备：" + jsonData.getInt("result_code") + ",结果：" + result_code);
		writeTEXT(CommonUtils.statusBeanToJson(true, "7004", "接收网关删除设备信息成功", null), response);
		
		if(ServletHashMap.ASYNC_CONTEXT_DELETE.containsKey(idGateway)){
			AsyncContext ac = ServletHashMap.ASYNC_CONTEXT_DELETE.get(idGateway);
			ac.complete();
		}
	}
		
}
