package cn.com.papi.smarthomesense.mapper;

import java.util.List;
import java.util.Map;

import cn.com.papi.smarthomesense.bean.UserBean;


public interface UserMapper {
	    //方法名必须和UserMapper中一致		
		
		public UserBean listByParams(Map<String,?> params) throws Exception;
		
		public List<UserBean> listAll() throws Exception;
		
}
