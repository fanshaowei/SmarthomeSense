package cn.com.papi.smarthomesense.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.papi.smarthomesense.service.ISenseDeviceStateLogService;

@Controller
@RequestMapping("senseDeviceStateLog")
public class SenseDeviceStateLogController {

	@Resource
	ISenseDeviceStateLogService senseDeviceStateLogService;
	
	@RequestMapping(value = "/getLogByTime/{time}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> getLogByTime(@PathVariable("time") String time){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("time", time);
		List<Map<String,Object>> list = senseDeviceStateLogService.getLogByTime(paramMap);
		
		return list;
	}
	
}
