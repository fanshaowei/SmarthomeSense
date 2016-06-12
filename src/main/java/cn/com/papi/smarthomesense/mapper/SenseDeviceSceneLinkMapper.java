package cn.com.papi.smarthomesense.mapper;

import java.util.List;
import java.util.Map;

import cn.com.papi.smarthomesense.bean.SenseDeviceSceneLink;

public interface SenseDeviceSceneLinkMapper {
    public int insert(SenseDeviceSceneLink senseDeviceSceneLink) throws Exception;

    public int delete(Map<String , Object> param) throws Exception;
    
    public List<SenseDeviceSceneLink> listAll() throws Exception;
    
    public List<SenseDeviceSceneLink> listByBean(SenseDeviceSceneLink senseDeviceSceneLink) throws Exception;
    
    public int update(SenseDeviceSceneLink senseDeviceSceneLink) throws Exception;
}
