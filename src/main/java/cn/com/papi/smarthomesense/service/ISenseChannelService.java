package cn.com.papi.smarthomesense.service;

import java.util.List;

import cn.com.papi.smarthomesense.bean.SenseChannelBean;

public interface ISenseChannelService {
	
    public Integer add(SenseChannelBean senseChannel) throws Exception;
    
    public List<SenseChannelBean> getByDeviceAndChannel(String idSenseDevice, String idSenseChannel) throws Exception;
    
    public List<SenseChannelBean> getByIdDevice(String idDevice) throws Exception;
    
    public Integer deleteByIdDevice(String idDevice) throws Exception;
    
}
