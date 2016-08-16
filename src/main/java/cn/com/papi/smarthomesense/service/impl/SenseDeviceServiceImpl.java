package cn.com.papi.smarthomesense.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.papi.smarthomesense.bean.SenseDeviceBean;
import cn.com.papi.smarthomesense.mapper.SenseDeviceMapper;
import cn.com.papi.smarthomesense.service.ISenseDeviceService;

@Service("senseDeviceService")
public class SenseDeviceServiceImpl implements ISenseDeviceService {
    @Resource
	SenseDeviceMapper senseDeviceMapper;
	
	@Override
	public List<SenseDeviceBean> getListByIdDevice(String idDevice) throws Exception {
	    Map<String,Object> params = new HashMap<String ,Object>();
	    params.put("idDevice",idDevice);		
		return senseDeviceMapper.listSenseDeviceByParams(params);
	}
	
	@Override
	public List<SenseDeviceBean> getListByIdGateway(String idGateway)
			throws Exception {
		Map<String,Object> params = new HashMap<String ,Object>();
	    params.put("idGateway",idGateway);
		return senseDeviceMapper.listSenseDeviceByParams(params);
	}
	
	@Override
	public Integer add(SenseDeviceBean device) throws Exception {				
		return senseDeviceMapper.insert(device);
	}

	@Override
	public Integer deleteByIdDevice(String idDevice) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idDevice", idDevice);		
		return senseDeviceMapper.delete(params);
	}



	@Override
	public SenseDeviceBean getSenseDeviceByIdDevice(String idDevice)
			throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idDevice", idDevice);
		SenseDeviceBean senseDeviceBean = senseDeviceMapper.getSenseDeviceByIdDevice(params);
		return senseDeviceBean;
	}

	@Override
	public Integer updateSenseDevice(SenseDeviceBean device) throws Exception {
			
		return senseDeviceMapper.update(device);
	}
		
	@Override
	public boolean isSenseDeviceExit(String idDevice) throws Exception{
		List<SenseDeviceBean> senseDeviceList = getListByIdDevice(idDevice);
		if(senseDeviceList != null && senseDeviceList.size() > 0){
			return true;
		}
		
		return false;
	}
	

}
