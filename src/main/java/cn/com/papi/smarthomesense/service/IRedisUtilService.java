package cn.com.papi.smarthomesense.service;

import java.util.List;
import java.util.Map;

public interface IRedisUtilService {
	//将token存进redis
	public String GetToken(String username);	
	
	public Object GetSceneCtrlJson(int idScene);
}
