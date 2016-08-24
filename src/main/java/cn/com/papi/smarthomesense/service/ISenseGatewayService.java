package cn.com.papi.smarthomesense.service;

import java.util.List;
import java.util.Map;

import cn.com.papi.smarthomesense.bean.SenseGatewayBean;

public interface ISenseGatewayService {

	List<SenseGatewayBean> getByIdGw(String idSenseGateway) throws Exception;
	
	public List<SenseGatewayBean> getGatewayFamily(String idGateway) throws Exception;
}
