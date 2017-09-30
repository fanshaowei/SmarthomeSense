package cn.com.papi.smarthomesense.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.papi.smarthomesense.bean.SenseDeviceStateLog;
import cn.com.papi.smarthomesense.mapper.SenseDeviceStateLogMapper;
import cn.com.papi.smarthomesense.service.ISenseDeviceStateLogService;

@Service("senseDeviceStateLogService")
public class SenseDeviceStateLogServiceImpl implements
		ISenseDeviceStateLogService {
    @Resource
    SenseDeviceStateLogMapper senseDeviceStateLogMapper; 
	
	@Override
	public Integer add(SenseDeviceStateLog senseDeviceStateLog)
			throws Exception {
		
		return senseDeviceStateLogMapper.insert(senseDeviceStateLog);
	}

	@Override
	public List<Map<String,Object>> getLogByTime(Map<String, String> paramMap){
		
		return senseDeviceStateLogMapper.list(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> getDeviceGroupTopOne(
			Map<String, String> paramMap) {
		return senseDeviceStateLogMapper.getDeviceGroupTopOne(paramMap);
	}
	
	@Override
	public List<Map<String, String>> getOneDeviceLimit(
			Map<String, Object> paramMap) {
		
		return senseDeviceStateLogMapper.getOneDeviceLimit(paramMap);
	}
}
