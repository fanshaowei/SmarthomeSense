package cn.com.papi.smarthomesense.mapper;

import java.util.List;

import cn.com.papi.smarthomesense.bean.SenseDeviceSceneRelate;

public interface SenseDeviceSceneRelateMapper {
    public List<SenseDeviceSceneRelate> getListByBean(SenseDeviceSceneRelate senseDeviceSceneRelate);
}
