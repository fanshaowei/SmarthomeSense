package cn.com.papi.smarthomesense.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.papi.smarthomesense.bean.SenseDeviceSceneLink;
import cn.com.papi.smarthomesense.mapper.SenseDeviceSceneLinkMapper;
import cn.com.papi.smarthomesense.service.ISenseDeviceSceneLinkService;

@Service("senseDeviceSceneLinkService")
public class SenseDeviceSceneLinkServiceImpl implements
		ISenseDeviceSceneLinkService {
    @Resource
    SenseDeviceSceneLinkMapper senseDeviceSceneLinkMapper;
	
	@Override
	public int add(SenseDeviceSceneLink senseDeviceSceneLink) throws Exception {				
		return senseDeviceSceneLinkMapper.insert(senseDeviceSceneLink);
	}

	@Override
	public int deleteById(int id) throws Exception {
		Map<String ,Object> param = new HashMap<String,Object>();
		param.put("id",id);
		
		return senseDeviceSceneLinkMapper.delete(param);
	}

	@Override
	public List<SenseDeviceSceneLink> getByIdDevice(String idDevice)
			throws Exception {
		SenseDeviceSceneLink senseDeviceSceneLink = new SenseDeviceSceneLink();
		senseDeviceSceneLink.setIdDevice(idDevice);
		
		return senseDeviceSceneLinkMapper.listByBean(senseDeviceSceneLink);
	}

}
