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
import cn.com.papi.smarthomesense.service.ISenseDeviceService;

@Controller
public class SenseDeviceInfoAction {
	@Resource
	ISenseDeviceService senseDeviceService;
	
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
}
