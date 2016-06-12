package cn.com.papi.smarthomesense.service;

import java.util.List;

import cn.com.papi.smarthomesense.bean.UserBean;


public interface IUserService {
	public static final String USER_UID = "uid";
	public static final String USER_PASSWORD = "password";
	public static final String USER_USERNAME = "username";

	/**
	 * 按uid查找
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	public UserBean getUserById(int uid) throws Exception;
	/**
	 * 按username查找
	 * @param username 用户名
	 * @return UserBean结果集 
	 * @throws Exception
	 */
	public UserBean getUserByUsername(String username) throws Exception;
	/**
	 * 列出USER表中所有数据
	 * @return User结果集
	 * @throws Exception 
	 */
	public List<UserBean> getAllUser() throws Exception;
	
	public UserBean getUserByUsernameAndPwd(String username, String password) throws Exception;
	
}
