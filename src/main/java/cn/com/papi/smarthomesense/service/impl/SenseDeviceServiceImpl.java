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
	
    /**
     * 根据设备id获取记录
     */
	@Override
	public List<SenseDeviceBean> getListByIdDevice(String idDevice) throws Exception {
	    Map<String,Object> params = new HashMap<String ,Object>();
	    params.put("idDevice",idDevice);		
		return senseDeviceMapper.listSenseDeviceByParams(params);
	}
	
	/**
	 * 根据网关id获取设备记录
	 */
	@Override
	public List<SenseDeviceBean> getListByIdGateway(String idGateway)
			throws Exception {
		Map<String,Object> params = new HashMap<String ,Object>();
	    params.put("idGateway",idGateway);
		return senseDeviceMapper.listSenseDeviceByParams(params);
	}
	
	/**
	 * 根据家庭id获取设备记录
	 */
	@Override
	public List<SenseDeviceBean> getListByIdFamily(int idFamily) throws Exception{
		Map<String,Object> params = new HashMap<String ,Object>();
	    params.put("idFamily",idFamily);
		return senseDeviceMapper.listSenseDeviceByParams(params);
	}
	
	/**
	 * 添加设备记录
	 */
	@Override
	public Integer add(SenseDeviceBean device) throws Exception {				
		return senseDeviceMapper.insert(device);
	}
    
	/**
	 * 根据设备id删除记录
	 */
	@Override
	public Integer deleteByIdDevice(String idDevice) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idDevice", idDevice);		
		return senseDeviceMapper.delete(params);
	}
	
	/**
	 * 根据网关id删除设备记录
	 */
	@Override
	public Integer deleteByIdGateway(String idGateway) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idGateway", idGateway);		
		
		return senseDeviceMapper.delete(params);
	}

	/**
	 * 根据家庭id删除设备记录
	 */
	@Override
	public Integer deleteByIdFamily(int idFamily) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idFamily", idFamily);
		return senseDeviceMapper.delete(params);
	}
	
	/**
	 * 根据设备id获取设备相关信息
	 */
	@Override
	public SenseDeviceBean getSenseDeviceByIdDevice(String idDevice)
			throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idDevice", idDevice);
		SenseDeviceBean senseDeviceBean = senseDeviceMapper.getSenseDeviceByIdDevice(params);
		return senseDeviceBean;
	}

	/**
	 * 更新设备信息
	 */
	@Override
	public Integer updateSenseDevice(SenseDeviceBean device) throws Exception {
			
		return senseDeviceMapper.update(device);
	}
	
	/**
	 * 更新设备的网关id
	 */
	@Override
	public Integer updateSenseDeviceGatewayId(Map<String,Object> params) {
		senseDeviceMapper.updatSenseDeviceGatewayId(params);
		return null;
	}
		
	/**
	 * 判断设备是否存在
	 */
	@Override
	public boolean isSenseDeviceExit(String idDevice) throws Exception{
		List<SenseDeviceBean> senseDeviceList = getListByIdDevice(idDevice);
		if(senseDeviceList != null && senseDeviceList.size() > 0){
			return true;
		}
		
		return false;
	}
}
