package cn.com.papi.smarthomesense.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.papi.smarthomesense.bean.SenseChannelBean;
import cn.com.papi.smarthomesense.mapper.SenseChannelMapper;
import cn.com.papi.smarthomesense.service.ISenseChannelService;

@Service("senseChannelService")
public class SenseChannelServiceImpl implements ISenseChannelService {
    @Resource
    SenseChannelMapper senseChannelMapper;
	
	@Override
	public Integer add(SenseChannelBean senseChannel) throws Exception{		
		return senseChannelMapper.insert(senseChannel);
	}

	@Override
	public List<SenseChannelBean> getByDeviceAndChannel(String idDevice,
			String idChannel) throws Exception {
		SenseChannelBean senseChannelBean = new SenseChannelBean();
		senseChannelBean.setIdDevice(idDevice);
		senseChannelBean.setIdChannel(idChannel);
		
		return senseChannelMapper.listByBean(senseChannelBean);
	}

	@Override
	public List<SenseChannelBean> getByIdDevice(String idDevice)
			throws Exception {
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("idDevice", idDevice);
		
		return senseChannelMapper.listByIdDevice(param);
	}

	@Override
	public Integer deleteByIdDevice(String idDevice) throws Exception {
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("idDevice", idDevice);
		
		return senseChannelMapper.deleteByIdDevice(param);
	}

}
