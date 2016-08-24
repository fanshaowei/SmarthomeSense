package cn.com.papi.smarthomesense.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.papi.smarthomesense.bean.SenseGatewayBean;
import cn.com.papi.smarthomesense.mapper.SenseGatewayMapper;
import cn.com.papi.smarthomesense.service.ISenseGatewayService;

@Service("senseGatewayService")
public class SenseGatewayServiceImpl implements ISenseGatewayService{

	@Resource
	SenseGatewayMapper senseGatewayMapper;
	
	@Override
	public List<SenseGatewayBean> getByIdGw(String idSenseGateway)throws Exception {
		Map<String,String> params = new HashMap<String,String>();
		params.put("idGateway", idSenseGateway);
				
		return senseGatewayMapper.listSenseGatewayByParams(params);
	}

	@Override
	public List<SenseGatewayBean> getGatewayFamily(String idGateway)
			throws Exception {
	    Map<String,String> params = new HashMap<String,String>();
	    params.put("idGateway", idGateway);
		
		return senseGatewayMapper.getGatewayFamily(params);
	}
	
}
