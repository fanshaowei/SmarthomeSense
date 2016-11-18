/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cn.com.papi.nettyserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

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

import cn.com.papi.smarthomesense.enums.SenseDeviceContorlUrl;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * @author fanshaowei
 *netty服务端，用来接收定时服务器 定时通过netty客户端发过过来的消息，并做相应的处理
 */
@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	    System.out.println("客户端 ："+ ctx.channel().remoteAddress() + "连接成功");
		super.channelActive(ctx);
	}
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {        
    	final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    	
    	if(((String)msg).equals("client ready connect to server!")){
    		
    	}else{
    	
	    	JSONObject jsonRead = JSONObject.fromObject(msg);
	    	String type = jsonRead.getString("type");
	    	if(type.equals("test")){
	    		System.out.println("----------" + df.format(new Date()) + "接收到任务消息：" + jsonRead.getString("jobName") + "----------");
	    	}
	    	    	
	    	if(type.equals("sceneCtl")){
	    		String username = jsonRead.getString("username");
	    		final String idScene = jsonRead.getString("sceneId");
	    		String reqToken = jsonRead.getString("reqToken");
	    		
	    		Properties properties = new Properties();
	    		InputStream inputStream = NettyServerHandler.class.getClassLoader().getResourceAsStream("smarthomeSenseConfig.properties");		
	    		try {
	    			properties.load(inputStream);
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    		    		
		    	//String testUrl = "http://127.0.0.1:9802/SmarthomeSense/quartzReqTest?jobName="+jobName;	
		    	String smarthomeUrl = properties.getProperty("Smarthome.url");
		    	String sceneUrl = smarthomeUrl + SenseDeviceContorlUrl.SCENE_CONTROL.getUrl(); 
		   	    sceneUrl = sceneUrl.replace(":username", username)
			       .replace(":reqToken", reqToken)
			       .replace(":idScene", idScene);
		    	
				final HttpGet httpGet = new HttpGet(sceneUrl);		
				RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000)
						.setConnectTimeout(3000)
						.build();		
			    CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
			    		.setDefaultRequestConfig(requestConfig).build();
				
				//发送请求
				try {
					httpClient.start();
					final CountDownLatch latch = new CountDownLatch(1);
					httpClient.execute(httpGet, new FutureCallback<HttpResponse>(){
						@Override
						public void completed(HttpResponse result) {
							latch.countDown();
							HttpEntity httpEntity = result.getEntity();
							if(httpEntity != null){
					    	    String entityString = null;
								try {
									entityString = EntityUtils.toString(httpEntity);
									System.out.println("-----情景控制"+ idScene +"任务完成返回时间:" + df.format(new Date()) + ",响应内容:" + entityString +"------");
								} catch (ParseException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}			    	    			    	    	    		   	    		 
							} 					
						}//end completed
		
						@Override
						public void failed(Exception ex) {
							latch.countDown();
							System.out.println("---------------任务:" + idScene +" 失败----------");
						}
		
						@Override
						public void cancelled() {
							latch.countDown();
							System.out.println("---------------任务:" + idScene +" 被取消");
						}
						
					});
					try {
						latch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}finally{
					try {
						httpClient.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	    	}
    	}	
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
