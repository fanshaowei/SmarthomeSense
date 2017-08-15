package cn.com.papi.smarthomesense.mapper;

import java.util.List;
import java.util.Map;

import cn.com.papi.smarthomesense.bean.SenseGatewayBean;

public interface SenseGatewayMapper {
    public Integer insert(SenseGatewayBean deivce);
    
    public List<SenseGatewayBean> listAllSenseGateway();
    
    public List<SenseGatewayBean> listSenseGatewayByParams(Map<String,String> params);
    
    public SenseGatewayBean getSenseGatewayByID(Map<String,String> params);
    
    public Integer update(SenseGatewayBean device);
    
    public List<SenseGatewayBean> getGatewayFamily(Map<String,String> params);
}
