package cn.com.papi.smarthomesense.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import cn.com.papi.common.util.RedisUtil;
import cn.com.papi.smarthomesense.service.IRedisUtilService;

@Service("redisUtilService")
public class RedisUtilServiceIpml implements IRedisUtilService{
	
	@Resource 
	RedisTemplate redisTemplate;
	/**
	 * token
	 */
	//token的存活期为30天
	//获取token
	public String GetToken(String username){
		RedisUtil redisUtil = new RedisUtil(redisTemplate);
		String keyToken = "T" + username;
		String token = (String) redisUtil.get(keyToken);
		return token;
	}
	
	//获取情景控制指令
	public Object GetSceneCtrlJson(int idScene){
		RedisUtil redisUtil = new RedisUtil(redisTemplate);
		String keySceneCtrl = "M" + idScene;
		return redisUtil.get(keySceneCtrl);
	}
}
