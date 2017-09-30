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
	
	@RequestMapping(value = "/getLogByTime/{idGateway}/{time}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> getLogByTime(@PathVariable("idGateway") String idGateway,
			@PathVariable("time") String time){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("idGateway",idGateway);
		paramMap.put("time", time);
		List<Map<String,Object>> list = senseDeviceStateLogService.getLogByTime(paramMap);
		
		return list;
	}
	
	@RequestMapping(value = "/getDeviceGroupTopOne/{idGateway}/{time}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> getDeviceGroupTopOne(@PathVariable("idGateway") String idGateway,
			@PathVariable("time") String time){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("idGateway",idGateway);
		paramMap.put("time", time);
		List<Map<String,Object>> list = senseDeviceStateLogService.getDeviceGroupTopOne(paramMap);
		
		return list;
	}
	
	@RequestMapping(value = "/getOneDeviceLimit/{idGateway}/{idDevice}/{time}/{offset}/{rows}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String,String>> getOneDeviceLimit(@PathVariable("idGateway") String idGateway, 
			@PathVariable("idDevice") String idDevice, @PathVariable("time") String time, 
			@PathVariable("offset") String offset, @PathVariable("rows") String rows){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("idGateway",idGateway);
		paramMap.put("idDevice",idDevice);
		paramMap.put("time", time);
		paramMap.put("offset", Integer.parseInt(offset));
		paramMap.put("rows", Integer.parseInt(rows));
		
		List<Map<String,String>> list = senseDeviceStateLogService.getOneDeviceLimit(paramMap);
		
		return list;
	}
}
