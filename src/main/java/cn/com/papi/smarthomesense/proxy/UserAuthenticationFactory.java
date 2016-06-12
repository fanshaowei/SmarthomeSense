package cn.com.papi.smarthomesense.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import cn.com.papi.smarthomesense.service.IUserAuthenticationService;
import cn.com.papi.smarthomesense.service.impl.UserAuthenticationServiceImpl;

public class UserAuthenticationFactory {
     
    public  static  IUserAuthenticationService getUserAuthenticationFactory(){
    	UserAuthenticationServiceImpl uai = new UserAuthenticationServiceImpl();
    	InvocationHandler ih = new ProxyHandler<UserAuthenticationServiceImpl>(uai);
    	
    	IUserAuthenticationService  userAuthenticationInterface =
        		(IUserAuthenticationService)Proxy.newProxyInstance(uai.getClass().getClassLoader(), uai.getClass().getInterfaces(), ih);
		
    	return userAuthenticationInterface;    	
    }
}
