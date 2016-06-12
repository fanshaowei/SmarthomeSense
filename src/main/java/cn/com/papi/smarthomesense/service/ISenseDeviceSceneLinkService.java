package cn.com.papi.smarthomesense.service;

import java.util.List;

import cn.com.papi.smarthomesense.bean.SenseDeviceSceneLink;

public interface ISenseDeviceSceneLinkService {
    
	public int add(SenseDeviceSceneLink senseDeviceSceneLink) throws Exception;
	
	public int deleteById(int id)  throws Exception;
	
	public List<SenseDeviceSceneLink> getByIdDevice(String idDevice) throws Exception;
}
