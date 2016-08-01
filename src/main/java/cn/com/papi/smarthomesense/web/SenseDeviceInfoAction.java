package cn.com.papi.smarthomesense.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
    	Map<String,Object> mapReturn = new HashMap<String,Object>();
    	List<SenseDeviceBean> senseDeviceBeanList = null;
    	try {
		    senseDeviceBeanList =  senseDeviceService.getListByIdGateway(idGateway);
		} catch (Exception e) {			
			e.printStackTrace();
		}    	
        
    	mapReturn.put("status", "sucess");
    	mapReturn.put("message", "获取设备数据成功");
    	mapReturn.put("data", senseDeviceBeanList);
    	
    	return mapReturn;
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
    	map.put("deviceTypeCode",deviceTypeCode);
    	map.put("deviceType", deviceType);
            	    	
    	return this.getReturnMap(true, "设备类型", map);
    }
}
