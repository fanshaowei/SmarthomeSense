package cn.com.papi.smarthomesense.web;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.papi.smarthomesense.enums.SenseDeviceContorlUrl;
import cn.com.papi.smarthomesense.service.IRedisUtilService;
import cn.com.papi.smarthomesense.utils.CommonUtils;

/**
 * 
 * @author fanshaowei
 * 每天定时情景控制接口
 */
@Controller
public class DailyTriggerSceneAction {
	private Logger logger = Logger.getLogger(DailyTriggerSceneAction.class.getName());
	
	@Resource
	IRedisUtilService redisUtilService;
	
	@RequestMapping(value="dailyTriggerScene",method=RequestMethod.GET)
	public void dailyTriggerScene(@RequestParam("idScene") String idScene, @RequestParam("username") String username,
			HttpServletResponse res){
		logger.info("---------------SmarthomeSense定时控制情景-----------------------");
		
		String reqToken = redisUtilService.GetToken(username); 
				
		String sceneUrl = SenseDeviceContorlUrl.SCENE_CONTROL.getUrl(); 
   	    sceneUrl = sceneUrl.replace(":username", username)
	       .replace(":reqToken", reqToken)
	       .replace(":idScene", idScene);	
		System.out.println(sceneUrl);
		
   	    //创建htt客户端
		CloseableHttpClient httpClient = HttpClients.createDefault();
		//设置请求方式及连接超时时间							
		HttpGet httpGet = new HttpGet(sceneUrl);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000)
				.setConnectTimeout(3000)
				.build();
		httpGet.setConfig(requestConfig);		
		
        HttpResponse response = null;        
       try{
    	   System.out.println("----------SmarthomeSense调用情景控制接口----------------------------");
    	   response = httpClient.execute(httpGet);
    	   HttpEntity httpEntity = response.getEntity();
    	   if(httpEntity != null){
    		   String entityString = EntityUtils.toString(httpEntity);
    		   System.out.println("----------SmarthomeSense调用情景控制接口 返回信息----------------------------");
    		   System.out.println(entityString);    		   
    		   
    		   CommonUtils.write(entityString, res);
    	   }    	       	       	   
       }catch(Exception ex){
    	   System.out.println("----------SmarthomeSense调用情景控制接口执行异常----------------------------");
    	   ex.printStackTrace();
       }finally{
    	   try {
    		   httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
       }//end try catch
       
	}
	
}
