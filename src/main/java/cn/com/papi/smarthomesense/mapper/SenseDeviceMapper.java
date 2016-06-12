package cn.com.papi.smarthomesense.mapper;

import java.util.List;
import java.util.Map;

import cn.com.papi.smarthomesense.bean.SenseDeviceBean;

public interface SenseDeviceMapper {
	
	//添加
	public Integer insert(SenseDeviceBean device) throws Exception;
	//查询
	public List<SenseDeviceBean> listAllSenseDevice() throws Exception;
	
	public List<SenseDeviceBean> listSenseDeviceByParams(Map<String,Object> params) throws Exception;
	
	public SenseDeviceBean getSenseDeviceByIdDevice(Map<String,Object> params) throws Exception;
	//删除
	public Integer delete(Map<String,Object> params) throws Exception;
}
