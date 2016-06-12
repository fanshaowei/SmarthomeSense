package cn.com.papi.smarthomesense.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.papi.smarthomesense.bean.SenseDeviceSceneLink;
import cn.com.papi.smarthomesense.bean.SenseDeviceStateLog;
import cn.com.papi.smarthomesense.enums.SenseDeviceType;
import cn.com.papi.smarthomesense.enums.SenseDeviceState;
import cn.com.papi.smarthomesense.service.IRedisUtilService;
import cn.com.papi.smarthomesense.service.ISenseDeviceSceneLinkService;
import cn.com.papi.smarthomesense.service.ISenseDeviceStateLogService;
import cn.com.papi.smarthomesense.utils.BaseAction;
import cn.com.papi.smarthomesense.utils.CommonUtils;
import cn.com.papi.smarthomesense.utils.DateUtils;


@Controller
public class MsFromGwController extends BaseAction{
	@Resource
	ISenseDeviceStateLogService senseDeviceStateLogService;	
	@Resource
	ISenseDeviceSceneLinkService senseDeviceSceneLinkService;
	@Resource
	IRedisUtilService iRedisUtilService;
	
    @RequestMapping(value = "/gwSendReceipt",method = RequestMethod.POST)
	public void gwSendReceipt( HttpServletRequest request, HttpServletResponse response) throws Exception{
    	String data = CommonUtils.ReqtoString(request);
		data=data.substring(10);
	    JSONObject jsonData = JSONObject.fromObject(data);
	    String idGateway = jsonData.getString("terminal_code");
	    final String idDevice = jsonData.getString("equipment_code");
	    int type = jsonData.getInt("type");
	    int state = jsonData.getInt("state");
	    int idChannel = jsonData.getInt("number");
	    String stateName = SenseDeviceState.getSenseDeviceState(state);//设备上报状态名
	    String deviceType = SenseDeviceType.getDeviceTypeName(CommonUtils.subDeviceTypeCode(idDevice));
	    
	    //记录上报记录
	    SenseDeviceStateLog senseDeviceStateLog = new SenseDeviceStateLog();
	    senseDeviceStateLog.setIdGateway(idGateway);
	    senseDeviceStateLog.setIdDevice(idDevice);
	    senseDeviceStateLog.setStateCode(state);
	    senseDeviceStateLog.setStateName(stateName);
	    senseDeviceStateLog.setDeviceType(deviceType);
	    
	    Date time = DateUtils.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
	    senseDeviceStateLog.setTime(time);
	    senseDeviceStateLogService.add(senseDeviceStateLog);
	    
	    //查找设备联动情景
	    List<SenseDeviceSceneLink> senseDeviceSceneLinkList = 
	    		senseDeviceSceneLinkService.getByIdDevice(idDevice);
	    int idScene = senseDeviceSceneLinkList.get(0).getIdScene();
	    
	    String redisSceneCtrlJson = (String)iRedisUtilService.GetSceneCtrlJson(idScene);
	    
	  /*  CloseableHttpClient httpclient = null;
	    HttpGet httpget=null;
	    CloseableHttpResponse httpResponse = null ;
	    HttpEntity httpEntity = null;
	    try{
	    	httpclient = HttpClients.createDefault();
	    	
	    	String url = "http://58.249.57.253:8011/smarthome/sceneControl?username=13580387033&reqToken=13580387033b8014852348f4d81b696db19d2e389121465226429007&idScene=590";		    
		    httpget = new HttpGet(url);
		    httpResponse = httpclient.execute(httpget);
		    httpEntity = httpResponse.getEntity();
		   
		    url = "http://58.249.57.253:8011/smarthome/sceneControl?username=13580387033&reqToken=13580387033b8014852348f4d81b696db19d2e389121465226429007&idScene=591";
		    Thread.sleep(5000);
		    httpget = new HttpGet(url);
		    httpResponse = httpclient.execute(httpget);
		    httpEntity = httpResponse.getEntity();		    
		    if (httpEntity != null) {
		    	System.out.println("---------------------------------------------------------");
		    	System.out.println(httpResponse.getStatusLine());
                // 打印响应内容长度    
                System.out.println("Response content length: " + httpEntity.getContentLength());  
                // 打印响应内容    
                System.out.println("Response content: " + EntityUtils.toString(httpEntity));  
                System.out.println("---------------------------------------------------------");
            }  
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally{
	    	try{	    		    	
	    		httpclient.close();
	    	}catch(IOException e){
	    		e.printStackTrace();
	    	}
	    }*/
	    String url1 = "http://58.249.57.253:8011/smarthome/sceneControl?username=13580387033&reqToken=135803870331a74d114182e4cb3a4be8e60794273c61465292043071&idScene=590";
	    String url2 = "http://58.249.57.253:8011/smarthome/sceneControl?username=13580387033&reqToken=135803870331a74d114182e4cb3a4be8e60794273c61465292043071&idScene=591";
	    
	    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000)
	    		.setConnectTimeout(3000).build();
	    final CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
	    		.setDefaultRequestConfig(requestConfig).build();
	    
	    try{
	    	httpclient.start();
	    	//存放请求的地址
	    	final HttpGet[] httpGets = new HttpGet[]{
	    		new HttpGet(url1),
	    		new HttpGet(url2)
	    	};
	    	
	    	ServletHashMap.HTTP_GET_REQUEST_SCENE.put(idDevice, httpGets);
	    	
	    	final CountDownLatch latch = new CountDownLatch(ServletHashMap.HTTP_GET_REQUEST_SCENE.get(idDevice).length);
	    	
	    	HttpClientContext localContext = HttpClientContext.create();
	    	localContext.setAttribute("HTTP_GET_REQUEST_SCENE", httpGets);	
	    	
	    	for(int i=0 ;i< ServletHashMap.HTTP_GET_REQUEST_SCENE.get(idDevice).length; i++){
	    		final int j = i;
	    		final HttpGet httpGet = ServletHashMap.HTTP_GET_REQUEST_SCENE.get(idDevice)[i];
	    		httpclient.execute(httpGet, localContext, new FutureCallback<HttpResponse>(){
	    			
					@Override
					public void completed(HttpResponse response) {
						latch.countDown();
						System.out.println("-------------------------------------------------------");
						System.out.println(httpGet.getRequestLine() + "->" + response.getStatusLine());
						
						HttpEntity httpEntity = response.getEntity();
						try {
							System.out.println("Response content: " + EntityUtils.toString(httpEntity));
						} catch (ParseException e) {						
							e.printStackTrace();
						} catch (IOException e) {					
							e.printStackTrace();
						}
						
						System.out.println("-------------------------------------------------------");
					}

					@Override
					public void failed(Exception ex) {
						latch.countDown();	
						System.out.println("-------------------------------------------------------");
						System.out.println("请求地址:" + httpGet.getURI() +" 失败");
						System.out.println(ex.getLocalizedMessage());
						System.out.println("-------------------------------------------------------");
						if(ServletHashMap.HTTP_GET_REQUEST_SCENE.contains(idDevice))
			   	        	ServletHashMap.HTTP_GET_REQUEST_SCENE.remove(idDevice);
					}

					@Override
					public void cancelled() {
						latch.countDown();
						System.out.println("-------------------------------------------------------");
						System.out.println("请求地址:" + httpGet.getURI() +"取消");						
						System.out.println("-------------------------------------------------------");
						if(ServletHashMap.HTTP_GET_REQUEST_SCENE.contains(idDevice))
			   	        	ServletHashMap.HTTP_GET_REQUEST_SCENE.remove(idDevice);
					}				
		    	});
	    		
	    		//延时任务，指定下个任务多久后才执行
	    		if(j == 0){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {							
						e.printStackTrace();
					}
				}
	    	}
	    	 latch.await();
	    	 System.out.println("-------------------------------------------------------");
	    	 System.out.println("Shutting down");
	    	 System.out.println("-------------------------------------------------------");
   	        if(ServletHashMap.HTTP_GET_REQUEST_SCENE.contains(idDevice))
   	        	ServletHashMap.HTTP_GET_REQUEST_SCENE.remove(idDevice);
	    }finally{
	    	httpclient.close();
	    }
	    
	}
    
	@RequestMapping("/gwSendRegisterDoneMsg")
	public void gwSendRegisterDoneMsg( HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		//网关上报注册设备结果
		System.out.println("---------------网关上报注册智能设备结果---------------");
		String data = CommonUtils.ReqtoString(request);
		data=data.substring(10);
		
        JSONObject jsonData = JSONObject.fromObject(data); 	
		int result_code = jsonData.getInt("result_code");
		String idGateway =  jsonData.getString("terminal_code");
		
		System.out.println("注册设备网关：" + jsonData.getInt("result_code") + "结果：" + result_code);
		writeTEXT(CommonUtils.statusBeanToJson(true, "7003", "接收注册设备信息成功", null), response);		
		
		if(ServletHashMap.ASYNC_CONTEXT_REGDEV.containsKey(idGateway)){
			AsyncContext ac = ServletHashMap.ASYNC_CONTEXT_REGDEV.get(idGateway);
			ac.complete();
		}
	}
	
	@RequestMapping("/gwDeleteDeviceMsg")
	public void gwDeleteDeviceMsg(HttpServletRequest request,HttpServletResponse response){
		System.out.println("---------------网关上报设备删除结果--------------------");
		String data = CommonUtils.ReqtoString(request);
		data=data.substring(10);
		
		JSONObject jsonData = JSONObject.fromObject(data); 	
		int result_code = jsonData.getInt("result_code");
		String idGateway =  jsonData.getString("terminal_code");
		System.out.println("删除网关设备：" + jsonData.getInt("result_code") + "结果：" + result_code);
		
		String keyAsyncResp = "del"+idGateway;
		if(ServletHashMap.ASYNC_CONTEXT_DELETE.containsKey(keyAsyncResp)){
			AsyncContext ac = ServletHashMap.ASYNC_CONTEXT_DELETE.get(keyAsyncResp);
			ac.complete();
		}
	}
}
