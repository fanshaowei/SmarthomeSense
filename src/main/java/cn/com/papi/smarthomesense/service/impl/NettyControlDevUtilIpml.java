package cn.com.papi.smarthomesense.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.papi.NettyClient.NettyClient;
import cn.com.papi.NettyClient.WebMessage.WebMessageBuilderImpl;
import cn.com.papi.NettyClient.protobuf.WebMessagePojo.WebMessage;
import cn.com.papi.smarthomesense.service.INettyControlDevUtil;



/**
 * Netty控制设备基础工具类
 * @author yangtairen
 *
 */

@Service("nettyControlDev")
public class NettyControlDevUtilIpml implements INettyControlDevUtil {
	
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" );
//            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

	//发送控制命令接口
	@Override
	public void NettySendMsg(NettyClient client, int ctrlType, String to,
			String data) throws Exception {
		//以http发给nettyServer
//		String httpStr = "http://121.40.200.150:10000/gwpush";
//		String param = "type="+ctrlType +"&gwid="+to+"&data="+data;
//		System.out.println("" + httpStr + param);
//		String getString = sendGet(httpStr,param);
//		System.out.println("netty Server 返回的数据：" + getString);
		
		//以TCP发给nettyserver
		WebMessageBuilderImpl impl = new WebMessageBuilderImpl();
		WebMessage msg = impl.WebMessageBuild(ctrlType, to, data);
		System.out.println(""+ data);
		System.out.println(""+ client);
		client.msgWrite(msg);
	}

}
