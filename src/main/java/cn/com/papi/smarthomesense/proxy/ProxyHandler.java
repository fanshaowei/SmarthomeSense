package cn.com.papi.smarthomesense.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler<T> implements InvocationHandler {
    //private Object UserAuthenticationInterfaceImpl;
	
    /*public ProxyHandler(Object delegateClass){
    	this.UserAuthenticationInterfaceImpl = delegateClass;
    }*/
    
	private T objectImpl;
    public ProxyHandler(T objectImpl){
    	this.objectImpl = objectImpl;
    }
    
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
        Object object = method.invoke(objectImpl,args);
		
		return object;
	}

}
