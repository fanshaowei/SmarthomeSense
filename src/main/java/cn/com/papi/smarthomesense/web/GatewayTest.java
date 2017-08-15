package cn.com.papi.smarthomesense.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.papi.NettyClient.NettyClient;
import cn.com.papi.smarthomesense.enums.DataPacketTypes;
import cn.com.papi.smarthomesense.service.INettyControlDevUtil;
import cn.com.papi.smarthomesense.utils.CommonUtils;

@Controller
public class GatewayTest {
	private static String getIP = "";
	private static String getPORT = "";
	public static NettyClient client ;
	@Resource
	private INettyControlDevUtil nettyControlDevUtil;
	
	static{
		Properties properties = CommonUtils.getNettyProperties();		
		getIP = properties.getProperty("netty.host");
		getPORT = properties.getProperty("netty.port");	
		client = new NettyClient(getIP,getPORT);
		/*Thread thread = new Thread(){			
			@Override
			public void run() {
				super.run();
				client.connect();
			}
		};	
		thread.setDaemon(true);
		thread.start();*/
	}
	
	@RequestMapping(value="/gatewayRequestTest",method = RequestMethod.POST)
	public @ResponseBody boolean GatewayRequestTest(HttpServletRequest request){
		String data = CommonUtils.ReqtoString(request);
		
	    JSONObject jsonData = JSONObject.fromObject(data);	    	    	  
	    
	    System.out.println( jsonData.getString("counter"));
	    
	    
	    try {
			nettyControlDevUtil.NettySendMsg(client, DataPacketTypes.SERVER_TO_GATEWAY_REG_DEVICE.getCodeType(), "0106118000008BBB", jsonData.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    return true;
	}
	
	@RequestMapping(value="/quartzReqTest",method = RequestMethod.GET)
	public @ResponseBody Callable<String> quartzReqTest(HttpServletRequest request, @RequestParam("jobName") String jobName){		
		final String thisjobName = jobName;
		Callable<String> callable=new Callable<String>(){
			@Override
			public String call() throws Exception {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
				System.out.println("------" + thisjobName + "执行请求时间:" + df.format(new Date()) + "------");
				return "scuess";
			}			
		};
		return callable;
	}
	
}
