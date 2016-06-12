package cn.com.papi.smarthomesense.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.papi.smarthomesense.bean.UserBean;
import cn.com.papi.smarthomesense.mapper.UserMapper;
import cn.com.papi.smarthomesense.service.IUserService;


@Service("userService")
public class UserServiceImpl implements IUserService{

	@Resource
	private UserMapper userMapper;
	
	@Override
	public UserBean getUserById(int uid) throws Exception {
		Map<String,Integer> params = new HashMap<>();
		params.put(USER_UID, uid);
		return userMapper.listByParams(params);
	}
	
	@Override
	public UserBean getUserByUsername(String username) throws Exception {
		Map<String,String> params = new HashMap<>();
		params.put(USER_USERNAME, username);
		return userMapper.listByParams(params);
	}
	@Override
	public List<UserBean> getAllUser() throws Exception {
		return userMapper.listAll();
	}

	@Override
	public UserBean getUserByUsernameAndPwd(String username, String password) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(USER_USERNAME, username);
		params.put(USER_PASSWORD, password);
		return userMapper.listByParams(params);
	}
}
