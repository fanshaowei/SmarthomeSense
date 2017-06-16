package cn.com.papi.smarthomesense.service.impl;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import cn.com.papi.common.config.SmarthomeSenseConfig;
import cn.com.papi.smarthomesense.bean.UserBean;
import cn.com.papi.smarthomesense.service.IRedisUtilService;
import cn.com.papi.smarthomesense.service.IUserAuthenticationService;
import cn.com.papi.smarthomesense.service.IUserService;
import cn.com.papi.smarthomesense.utils.CommonUtils;
 
@Service("userAuthenticationService")
public class UserAuthenticationServiceImpl implements IUserAuthenticationService {
	
	@Resource
	private IUserService userService;
	@Resource
	private IRedisUtilService redisUtilService;
	@Resource
	private SmarthomeSenseConfig smarthomeSenseConfig;	
	
	@Override
	public String userNameTokenAuthentication(HttpServletRequest request, String reqString) {
		//ApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
		//IUserService userService = (IUserService) ac.getBean("userService");
		//IRedisUtilService  redisUtilService = (IRedisUtilService) ac.getBean("redisUtilService");
		
		String returnStr = null;
		if("dev".equals(smarthomeSenseConfig.getSmarthomeActive())){
			return null;
		}
		
		try {			
			JSONObject json = JSONObject.fromObject(reqString);			
			String userName = json.getString("username");
			String token = json.getString("req_token");
			
			UserBean userBean = userService.getUserByUsername(userName);
			if(userBean == null ){
				returnStr = CommonUtils.statusBeanToJson(false, "9999", "用户不存在", null);
				return returnStr;				
			}else{		
				String redisToken = redisUtilService.GetToken(userName);
				if(!token.equals(redisToken)){
					returnStr = CommonUtils.statusBeanToJson(false, "9999", "由于该用户名已经在其他地方登录，您已被迫下线", null); 
					return returnStr;
				}
			}
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return returnStr;
	}

}
