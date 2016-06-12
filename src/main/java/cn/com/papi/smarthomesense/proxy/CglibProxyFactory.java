package cn.com.papi.smarthomesense.proxy;

import org.springframework.cglib.proxy.Enhancer;

public class CglibProxyFactory {
	
    public static Object getCglibProxyObj(String clazz) {
    	Class<?> superClass = null;
		try {
			superClass = Class.forName(clazz);
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
    	Enhancer hancer = new Enhancer();
    	//设置代理对象的父类 
    	hancer.setSuperclass(superClass);
    	//设置回调对象，即调用代理对象里面的方法时，实际上执行的是回调对象（里的intercept方法）。  
    	hancer.setCallback(new CglibMethodInterceptor());
    	//创建代理对象
    	return hancer.create();
    }
}
