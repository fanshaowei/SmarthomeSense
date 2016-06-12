package cn.com.papi.smarthomesense.web;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SenseDevicSceneLinkAction {
    
	//获取输入设备 与 输出设备 关联信息 
	@RequestMapping(value="/getSenseDeviceSceneLinkList",method=RequestMethod.GET)
	public Map<String,Object> getSenseDeviceSceneLinkList(){
		
		
		return null;
	}
	
}
