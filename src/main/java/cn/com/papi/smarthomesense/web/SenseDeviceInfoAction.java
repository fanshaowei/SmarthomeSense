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

@Controller
public class SenseDeviceInfoAction extends BaseAction{
	@Resource
	ISenseDeviceService senseDeviceService;
	
	/**
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
    
    /**
     * 根据设备id返设备类型
     * @param idDevice
     * @return
     */
    @RequestMapping(value="/getSenseDeviceType",method=RequestMethod.GET)
    public @ResponseBody Map<String,Object> getSenseDeviceType(@RequestParam("idDevice") String idDevice){
    	String deviceTypeCode = CommonUtils.subDeviceTypeCode(idDevice);
    	String deviceType = SenseDeviceType.getDeviceTypeName(deviceTypeCode);
    	
        HashMap<String,String> map = new HashMap<String,String>(); 
        if(deviceType != null && deviceType != ""){
        	map.put("deviceTypeCode",deviceTypeCode);
        	map.put("deviceType", deviceType);
        	
        	return this.getReturnMap(true, "设备ID输入正确", map);
        }
            	    	
    	return this.getReturnMap(false, "设备ID输入错误", null);
    }
    
    
    /**
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
}
