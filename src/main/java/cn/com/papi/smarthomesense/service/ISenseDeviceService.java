package cn.com.papi.smarthomesense.service;

import java.util.List;
import java.util.Map;

import cn.com.papi.smarthomesense.bean.SenseDeviceBean;


public interface ISenseDeviceService {
	
	//查找
	public List<SenseDeviceBean> getListByIdDevice(String idDevice) throws Exception;
	
	public List<SenseDeviceBean> getListByIdGateway(String idGateway) throws Exception;
	
	public List<SenseDeviceBean> getListByIdFamily(int idFamily) throws Exception;
	
	public SenseDeviceBean getSenseDeviceByIdDevice(String idDevice) throws Exception;
	
	//添加 
	public Integer add(SenseDeviceBean device) throws Exception;
	
	//删除
	public Integer deleteByIdDevice(String idDevice) throws Exception;
	
	public Integer deleteByIdGateway(String idGateway) throws Exception;
	
	public Integer deleteByIdFamily(int idFamily) throws Exception;
	
	//编辑
	public Integer updateSenseDevice(SenseDeviceBean device) throws Exception;
	
	public Integer updateSenseDeviceGatewayId(Map<String,Object> params);
	
	//根据id判断设备是否存在
	public boolean isSenseDeviceExit(String idDevice) throws Exception;
}
