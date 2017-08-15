package cn.com.papi.smarthomesense.service;

import java.util.List;
import java.util.Map;

import cn.com.papi.smarthomesense.bean.SenseDeviceStateLog;

public interface ISenseDeviceStateLogService {
   public Integer add(SenseDeviceStateLog senseDeviceStateLog) throws Exception;
   
   public List<Map<String,Object>> getLogByTime(Map<String, String> paramMap);
}
