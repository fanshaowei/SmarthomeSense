package cn.com.papi.smarthomesense.web;

import java.io.IOException;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.papi.smarthomesense.enums.SenseDeviceContorlUrl;
import cn.com.papi.smarthomesense.service.IRedisUtilService;

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
	public JSONObject dailyTriggerScene(@RequestParam("sceneId") String sceneId, @RequestParam("name") String username){
		logger.info("---------------定时控制情景-----------------------");
		
		JSONObject jsonObject = null;
		
		String reqToken = redisUtilService.GetToken(username); 
				
		String sceneUrl = SenseDeviceContorlUrl.SCENE_CONTROL.getUrl(); 
   	    sceneUrl = sceneUrl.replace(":username", username)
	       .replace(":reqToken", reqToken)
	       .replace(":idScene", sceneId);	
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(sceneUrl);		
        HttpResponse response = null;
        
       try{
    	   response = httpClient.execute(httpGet);
    	   HttpEntity httpEntity = response.getEntity();
    	   if(httpEntity != null){
    		   String entityString = EntityUtils.toString(httpEntity);
    		   System.out.print("--------------------------------------");
    		   System.out.print(entityString);
    		   System.out.print("--------------------------------------");
    		   
    		   jsonObject = JSONObject.fromObject(entityString);
    	   }    	       	       	   
       }catch(Exception ex){
    	   System.out.print("--------------------------------------");
		   System.out.print("定时控制情景失败");
		   System.out.print("--------------------------------------");
    	   ex.printStackTrace();
       }finally{
    	   try {
    		   httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
       }//end try catch
       
       return jsonObject;
	}
	
}
