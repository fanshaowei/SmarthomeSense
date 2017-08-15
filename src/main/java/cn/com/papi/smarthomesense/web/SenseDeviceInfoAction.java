package cn.com.papi.smarthomesense.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.papi.smarthomesense.bean.SenseDeviceBean;
import cn.com.papi.smarthomesense.enums.SenseDeviceType;
import cn.com.papi.smarthomesense.service.ISenseDeviceService;
import cn.com.papi.smarthomesense.utils.BaseAction;
import cn.com.papi.smarthomesense.utils.CommonUtils;

/**
 * 
 * @author fanshaowei
 *1、用于给 原生app对智能设备的操作 的调用
 *2、提供h5页面的相应接口
 */
@Controller
public class SenseDeviceInfoAction extends BaseAction{
	@Resource
	ISenseDeviceService senseDeviceService;
	
	/** app调用
	 * 根据网关id 查询网关下的设备
	 * @param idGateway
	 * @return
	 */
    @RequestMapping(value = "/getSenseDeviceList",method=RequestMethod.GET)//
	public @ResponseBody Map<String,Object> getSenseDeviceInfo(@RequestParam("idGateway") String idGateway){    
    	List<SenseDeviceBean> senseDeviceBeanList = null;
    	try {
		    senseDeviceBeanList =  senseDeviceService.getListByIdGateway(idGateway);
		} catch (Exception e) {			
			e.printStackTrace();
		}    	
    	
    	return this.getReturnMap(true, "获取设备数据成功", senseDeviceBeanList);
    }
    
    /** app调用
     * 根据设备id返设备类型
     * @param idDevice
     * @return
     */
    @RequestMapping(value="/getSenseDeviceType",method=RequestMethod.GET)
    public @ResponseBody Map<String,Object> getSenseDeviceType(@RequestParam("idDevice") String idDevice){
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	
    	String deviceTypeCode = CommonUtils.subDeviceTypeCode(idDevice);
    	String deviceType = SenseDeviceType.getDeviceTypeName(deviceTypeCode);
    	
    	switch (deviceTypeCode) {
		case "8211"://门磁					 
			map.put("state", "1");
			break;	
			
		case "0521":case "4521"://温湿度							
			map.put("temperature", getDeviceJson("120", "-40", "℃", "温度"));
			map.put("humidity", getDeviceJson("99.9", "0", "%", "湿度"));
			break;
		
		case "0531":case "4531"	://可燃气体
			map.put("methane", getDeviceJson("1000", "0", "ppm", "甲烷气体浓度"));
			map.put("carbon_monoxide", getDeviceJson("1000", "0", "ppm", "一氧化碳气体浓度"));
			break;
		
		case "0511"://人体红外
			map.put("state", "1");
			map.put("illuminance", getDeviceJson("2000", "3", "LX", "亮度"));
			break;
			
		default:
			break;
    	}
    	
        if(deviceType != null && deviceType != "" && !deviceType.equals("末定义设备")){
        	map.put("deviceTypeCode",deviceTypeCode);
        	map.put("deviceType", deviceType);
        	
        	return this.getReturnMap(true, "设备ID输入正确", map);
        }
            	    	
    	return this.getReturnMap(false, "设备ID输入错误", null);
    }
    
    private JSONObject getDeviceJson(String max,String min,String unit,String name){
    	JSONObject json = new JSONObject();
    	
    	json.accumulate("max", max);
		json.accumulate("min", min);
		json.accumulate("unit", unit);
		json.accumulate("name", name);
		
		return json;
    }
    
    
    /** app调用
     * 更新智能设备信息
     * @param request
     * @return
     */
    @RequestMapping(value="/editSenseDeviceInfo",method=RequestMethod.POST)
    public @ResponseBody Map<String,Object> editSenseDeviceInfo(HttpServletRequest request){
    	String data = CommonUtils.ReqtoString(request);
    	JSONObject json = JSONObject.fromObject(data);
    	
    	String idDevice = json.getString("idDevice");
    	String nameDevice = json.getString("nameDevice");
    	
    	SenseDeviceBean senseDeviceBean = new SenseDeviceBean();
    	senseDeviceBean.setIdDevice(idDevice);
    	senseDeviceBean.setNameDevice(nameDevice);
    	
    	try {
			int num = senseDeviceService.updateSenseDevice(senseDeviceBean);
			
			if(num > 0){
				return this.getReturnMap(true, "更新智能设备信息成功", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return this.getReturnMap(false, "更新智能设备信息失败", null);    	
    }
    
    /**
     * app调用
     * 删除网关时，把网关下的设备一起删除
     * @param idGateway
     * @return
     */
    @RequestMapping(value="/delSenseDeviceAsDelGateway",method=RequestMethod.POST)
    public @ResponseBody Map<String,Object> delSenseDeviceAsDelGateway(HttpServletRequest request){
    	String data = CommonUtils.ReqtoString(request);
    	JSONObject json = JSONObject.fromObject(data);
    	String idGateway = json.getString("idGateway");
    	try {
			 senseDeviceService.deleteByIdGateway(idGateway);					
		} catch (Exception e) {					
			e.printStackTrace();
			return this.getReturnMap(false, "删除网关下的智能设备失败", null);
		}
    	
    	return this.getReturnMap(true, "成功删除网关下的智能设备", null);
    }
    
    /**
     * app调用
     * 删除家庭时，把家庭下的设备一起删除
     * @param idGateway
     * @return
     */
    @RequestMapping(value="/delSenseDeviceAsDelFamily",method=RequestMethod.POST)
    public @ResponseBody Map<String,Object> delSenseDeviceAsDelFamily(HttpServletRequest request){
    	String data = CommonUtils.ReqtoString(request);
    	JSONObject json = JSONObject.fromObject(data);
    	int idFamily = json.getInt("idFamily");
    	try {
			 senseDeviceService.deleteByIdFamily(idFamily);					
		} catch (Exception e) {					
			e.printStackTrace();
			return this.getReturnMap(false, "删除网关下的智能设备失败", null);
		}
    	
    	return this.getReturnMap(true, "成功删除网关下的智能设备", null);
    }
    
    /**
     * h5调用
     * 获取家庭下的设备
     */
    @RequestMapping(value="/getFamilySenseDevice",method=RequestMethod.GET)
    public @ResponseBody Map<String,Object> getFamilySenseDevice(@RequestParam("idFamily") int idFamily){
    	List<SenseDeviceBean> senseDeviceBeanList = null;
    	try {
		    senseDeviceBeanList =  senseDeviceService.getListByIdFamily(idFamily);
		} catch (Exception e) {			
			e.printStackTrace();
		}    	
    	
    	return this.getReturnMap(true, "获取设备数据成功", senseDeviceBeanList);
    	
    }
}
