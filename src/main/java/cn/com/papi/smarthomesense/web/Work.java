package cn.com.papi.smarthomesense.web;

import javax.servlet.AsyncContext;

public class Work extends Thread{
	@SuppressWarnings("unused")
	private AsyncContext context;
	
	public Work(AsyncContext context){
		this.context = context;
	}
	
	@Override
	public void run()
	{
		try {
			Thread.sleep(4*1000);
//			PrintWriter writer = context.getResponse().getWriter();
//			writer.write("{\"data\":\"\",\"code\":\"6000\",\"msg\":\"this is async message\",\"status\":true}");
//			writer.flush();
//			context.complete();   //异步结束
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
