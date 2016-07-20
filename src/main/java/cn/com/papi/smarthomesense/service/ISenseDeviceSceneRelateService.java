package cn.com.papi.smarthomesense.service;

import java.util.List;

import cn.com.papi.smarthomesense.bean.SenseDeviceSceneRelate;

public interface ISenseDeviceSceneRelateService {
    public List<SenseDeviceSceneRelate> getListByBean(SenseDeviceSceneRelate senseDeviceSceneRelate);
    
    public void senseDeviceSceneRelateAction(SenseDeviceSceneRelate senseDeviceSceneRelate);
}
