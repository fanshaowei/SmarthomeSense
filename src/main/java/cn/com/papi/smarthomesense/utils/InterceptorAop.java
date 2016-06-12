package cn.com.papi.smarthomesense.utils;

import org.aspectj.lang.ProceedingJoinPoint;

public class InterceptorAop {
	 public void doBefore() {
		         System.out.println("========执行前置通知==========");
		     }
		     
		     public void doAferReturning() {
		         System.out.println("=========执行后置通知================");
		     }
		     
		     public void doAfter() {
		         System.out.println("========执行最终通知==========");
		     }
		     
		     public void doAferThrowing() {
		         System.out.println("=============执行意外通知================");
		     }
		     
		     public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		         
		         System.out.println("=========进入判断方法===========");
		         Object result = pjp.proceed();
		         System.out.println("==========进入退出方法==========");
		         return result;
		     }
}
