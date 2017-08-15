package cn.com.papi.smarthomesense.mapper;

import java.util.List;
import java.util.Map;

import cn.com.papi.smarthomesense.bean.SenseDeviceStateLog;

public interface SenseDeviceStateLogMapper {
    public Integer insert(SenseDeviceStateLog senseDeviceStateLog) throws Exception;
    
    public List<Map<String,Object>> list(Map<String, String> paramMap);
}
