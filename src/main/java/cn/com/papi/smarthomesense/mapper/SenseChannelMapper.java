package cn.com.papi.smarthomesense.mapper;

import java.util.List;
import java.util.Map;

import cn.com.papi.smarthomesense.bean.SenseChannelBean;

public interface SenseChannelMapper {
	//添加
	public Integer insert(SenseChannelBean channel) throws Exception;
	//删除
	public Integer deleteByChannel(Map<String ,Object> params) throws Exception;
	
	public Integer deleteByBean(SenseChannelBean channel) throws Exception;
			
	public Integer deleteByIdDevice(Map<String, Object> params) throws Exception;
	//查找
	public List<SenseChannelBean> listByBean(SenseChannelBean channel) throws Exception;
	
	public List<SenseChannelBean> listByIdDevice(Map<String,Object> params)throws Exception;
	//更新
	public  Integer update(SenseChannelBean channel) throws Exception;
}
