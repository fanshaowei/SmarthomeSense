package cn.com.papi.smarthomesense.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.papi.common.config.NettyConfig;
import cn.com.papi.nettyserver.NettyServer;

/**
 * 
 * @author fanshaowei
 *
 *该类在web容器启动时调用，用来启动nettyServer,接收定时服务器上nettyClient定时发送过来的
 *情景控制信息
 */
public class InitNettyListener implements ServletContextListener{
	private Logger logger = Logger.getLogger(InitNettyListener.class.getName());
	
	private ApplicationContext ac = null;
	private NettyConfig nettyConfig;
	
	private Thread nettyThread;
	
	//获取spring注入的bean
	private void getContextBean(ServletContext servletContext)
	{
		ac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		this.nettyConfig = (NettyConfig) ac.getBean("nettyConfig");
	}
		
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.info("---------------启动nettyServer---------------");
		
		final ServletContext servletContext = servletContextEvent.getServletContext();
		getContextBean(servletContext);
		
		nettyThread = new Thread(){
			@Override
			public void run() {
				super.run();
				NettyServer.initNettyServer(nettyConfig.getHostServer(), nettyConfig.getPortServer());
			}
		};	
		nettyThread.setDaemon(true);
		nettyThread.setName("nettyServer");
		nettyThread.start();		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.info("---------------关闭nettyServer---------------");				
		NettyServer.closeNettyServer();
		
		System.out.println(nettyThread);
		nettyThread = null;
	}

}
