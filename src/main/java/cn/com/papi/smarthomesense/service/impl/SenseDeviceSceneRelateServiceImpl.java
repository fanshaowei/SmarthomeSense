package cn.com.papi.smarthomesense.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.com.papi.common.config.SmarthomeSenseConfig;
import cn.com.papi.smarthomesense.bean.SenseDeviceSceneRelate;
import cn.com.papi.smarthomesense.enums.SenseDeviceContorlUrl;
import cn.com.papi.smarthomesense.mapper.SenseDeviceSceneRelateMapper;
import cn.com.papi.smarthomesense.service.IRedisUtilService;
import cn.com.papi.smarthomesense.service.ISenseDeviceSceneRelateService;

@Service("senseDeviceSceneRelateService")
public class SenseDeviceSceneRelateServiceImpl implements ISenseDeviceSceneRelateService {
	Logger logger = Logger.getLogger(SenseDeviceSceneRelateServiceImpl.class.getName());
	
    @Resource
    SenseDeviceSceneRelateMapper senseDeviceSceneRelateMapper;
    @Resource
	IRedisUtilService redisUtilService;
    @Resource
    SmarthomeSenseConfig smarthomeSenseConfig;
    

	/**
     * 查询记录
     */
	@Override
	public List<SenseDeviceSceneRelate> getListByBean(SenseDeviceSceneRelate senseDeviceSceneRelate) {						
		
		return senseDeviceSceneRelateMapper.getListByBean(senseDeviceSceneRelate);
	}
	
	/**
	 * 智能设备情景关联执行动作
	 */
	@Override
	public void senseDeviceSceneRelateAction(SenseDeviceSceneRelate senseDeviceSceneRelate){
		String smarthomeUrl = smarthomeSenseConfig.getSmarthomeUrl();
		
		String isValid = senseDeviceSceneRelate.getIsValid(); 
		//判断执行关联情景是否有效 0：无效 1：有效
	    if(isValid.equals("0")){	    	
	    	logger.info("------------------非执行情景关联的有效时间段。。。------------------");
	    	
	    	return;
	    }
		
		String sceneJson = senseDeviceSceneRelate.getSceneJson();
	    JSONArray doSceneArray = JSONObject.fromObject(sceneJson).getJSONArray("doScene");
	    String sceneId = "";//情景id
	    String delayTime = "0";	//情景延时时间     
		String username = JSONObject.fromObject(sceneJson).getString("username");//用户名
		String reqToken = redisUtilService.GetToken(username);//token
		
		//拼凑请求的相关信息：url，延时
		List<Map<String, Object>> requestList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<doSceneArray.size();i++){	 	    	
	    	 //拼凑请求情景控制的url
	    	 sceneId = doSceneArray.getJSONObject(i).getString("sceneId");
	    	 delayTime = doSceneArray.getJSONObject(i).getString("delayTime");
	    	 if(delayTime==null || delayTime.equals("")){
	    		 delayTime = "0";
	    	 }
	    	 String sceneUrl = smarthomeUrl + SenseDeviceContorlUrl.SCENE_CONTROL.getUrl(); 
	    	 sceneUrl = sceneUrl.replace(":username", username)
		       .replace(":reqToken", reqToken)
		       .replace(":idScene", sceneId);

	    	 Map<String,Object> map = new HashMap<String,Object>();
	    	 map.put("url", new HttpGet(sceneUrl));
	    	 map.put("delayTime", delayTime);
	    	 requestList.add(map);
		}
		
		//设置边接超时时间5s，服务器返回超时为5s
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000)
	    		.setConnectTimeout(5000).build();
	    //创建一个异步http客户端
	    final CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
	    		.setDefaultRequestConfig(requestConfig).build();		    
	    try{
	    	//启动httpclient
	    	httpclient.start();
	    	//异步请求计数器，当latch为0时，释放线程
    	    final CountDownLatch latch = new CountDownLatch(requestList.size());	    	 
    	 	
    	    //循环发送情景控制请求
    	    for(Map<String, Object> map: requestList){
    	    	//延时任务，指定下个任务多久后才执行=    	    	
    	    	try {
					Thread.sleep(Integer.valueOf(map.get("delayTime").toString()) * 1000);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
    	    	
    	    	final HttpGet httpGet = (HttpGet)map.get("url");
    	    	httpclient.execute(httpGet, new FutureCallback<HttpResponse>() {                
    				@Override
    				public void completed(HttpResponse response) {
    					latch.countDown();
    					    					
    					logger.info("------------------" + httpGet.getRequestLine() + "->" + response.getStatusLine() + "------------------");
    									    					
    					HttpEntity httpEntity = response.getEntity();
    					try {
    						logger.info("Response content: " + EntityUtils.toString(httpEntity));    						
    					} catch (ParseException e) {						
    						e.printStackTrace();
    					} catch (IOException e) {					
    						e.printStackTrace();
    					}					
    				}

    				@Override
    				public void failed(Exception ex) {					
    					latch.countDown();    					
    					logger.info("------------------情景关联:" + httpGet +" 失败------------------");
    					logger.info(ex.getLocalizedMessage());    					    				
    				}

    				@Override
    				public void cancelled() {
    					latch.countDown();    					
    					logger.info("------------------情景关联:" + httpGet +" 被取消------------------");						    					    					    			
    				}    			
    	    	 });
    	    	    	    	
    	    }//end for
    	    try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	    logger.info("------------------情景关联 Shutting down------------------");
	    }finally{
	    	try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
	}

}
