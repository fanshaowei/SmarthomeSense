package cn.com.papi.smarthomesense.service;

import java.util.List;
import java.util.Map;

import cn.com.papi.smarthomesense.bean.SenseDeviceBean;


public interface ISenseDeviceService {
	public static final String DEVICE_ID="idDevice";
	public static final String GATEWAY_ID="idGateway";
	public static final String IS_ACTIVE="isActive";
	public static final String DEVICE_TYPE="typeDevice";
	public static final String DEVICE_NUMBER="numDevice";
	public static final String GROUP_ID="idGroup";
	public static final String ID="id";
	
	//查找
	public List<SenseDeviceBean> getListByIdDevice(String idDevice) throws Exception;
	
	public List<SenseDeviceBean> getListByIdGateway(String idGateway) throws Exception;
	
	public SenseDeviceBean getSenseDeviceByIdDevice(String idDevice) throws Exception;
	
	//添加 
	public Integer add(SenseDeviceBean device) throws Exception;
	
	//删除
	public Integer deleteByIdDevice(String idDevice) throws Exception;
}
