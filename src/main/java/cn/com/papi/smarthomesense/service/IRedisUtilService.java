package cn.com.papi.smarthomesense.service;


public interface IRedisUtilService {
	//将token存进redis
	public String GetToken(String username);	
	
	public Object GetSceneCtrlJson(int idScene);
}
