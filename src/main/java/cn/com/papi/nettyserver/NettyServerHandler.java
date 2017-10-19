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
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

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
	private Logger logger = Logger.getLogger(NettyServerHandler.class);
			
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("客户端 ："+ ctx.channel().remoteAddress() + "连接成功");
		super.channelActive(ctx);
	}
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException, IOReactorException {        
    	final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    	
    	if(((String)msg).equals("QuartzServer_Heartbeat!")){
    		logger.info("----------------" + (String)msg + "-------------------");
    	}else if(((String)msg).equals("client ready connect to server!")){
    		logger.info("----------------" + (String)msg + "-------------------");
    	}else{    	
	    	final JSONObject jsonRead = JSONObject.fromObject(msg);
	    	String type = jsonRead.getString("type");	    	    		    	
	    	
	    	if(type.equals("sceneCtl")){//test	    		
	    		String username = jsonRead.getString("username");
	    		final String idScene = jsonRead.getString("sceneId");
	    		String reqToken = jsonRead.getString("reqToken");
	    		final String jobName = jsonRead.getString("jobName");
	    		
	    		Properties properties = new Properties();
	    		InputStream inputStream = NettyServerHandler.class.getClassLoader().getResourceAsStream("smarthomeSenseConfig.properties");		
	    		try {
	    			properties.load(inputStream);
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    		    			
		    	String smarthomeUrl = properties.getProperty("Smarthome.url");
		    	String sceneUrl = smarthomeUrl + SenseDeviceContorlUrl.SCENE_CONTROL.getUrl(); 
		    	logger.info(sceneUrl);
		   	    sceneUrl = sceneUrl.replace(":username", username)
			       .replace(":reqToken", reqToken)
			       .replace(":idScene", idScene);
		    	
				final HttpGet httpGet = new HttpGet(sceneUrl);		
				RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000)
						.setConnectTimeout(3000)
						.build();	
				
				ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
		        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
		        cm.setMaxTotal(100);
			    CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom()
			    		.setConnectionManager(cm)
			    		.setDefaultRequestConfig(requestConfig)
			    		.build();
				
			    logger.info("-----情景定时控制:"+ jobName +",开始执行时间:" + df.format(new Date()) );
				//发送请求
				httpAsyncClient.start();
				final CountDownLatch latch = new CountDownLatch(1);
				httpAsyncClient.execute(httpGet, new FutureCallback<HttpResponse>(){
					@Override
					public void completed(HttpResponse result) {
						latch.countDown();
						HttpEntity httpEntity = result.getEntity();
						if(httpEntity != null){
				    	    String entityString = null;
							try {
								entityString = EntityUtils.toString(httpEntity);									
								logger.info("-----情景定时控制:"+ jobName +",任务完成返回时间:" + df.format(new Date()) + ",响应内容:" + entityString +"------");
							} catch (Exception e) {
								e.printStackTrace();
							}			    	    			    	    	    		   	    		 
						} 					
					}//end completed
	
					@Override
					public void failed(Exception ex) {
						latch.countDown();
						System.out.println("---------------情景定时控制:" + jobName +" 失败----------");
					}
	
					@Override
					public void cancelled() {
						latch.countDown();
						System.out.println("---------------情景定时控制:" + jobName +" 被取消");
					}
					
				});
				
				try {
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				try {
					httpAsyncClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
						
			}//end if
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
