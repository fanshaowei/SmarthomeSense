package cn.com.papi.smarthomesense.service.impl;

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

}
