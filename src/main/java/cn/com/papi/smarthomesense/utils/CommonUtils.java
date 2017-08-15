package cn.com.papi.smarthomesense.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import cn.com.papi.smarthomesense.bean.StatusBean;

import com.google.gson.Gson;


public class CommonUtils {

	public static String statusBeanToJson(boolean status, String code, String msg,
			Object data) {
		StatusBean<Object> sb = new StatusBean<>(status, code, msg, data);
		Gson gson = new Gson();
		String json = gson.toJson(sb);
		return json;
	}
	
	public static String subDeviceTypeCode(String idSenseDevice){
		return (String) idSenseDevice.subSequence(2, 6);
	}
	
    public static String file2String(File f, String charset) { 
        String result = null; 
        try { 
                result = stream2String(new FileInputStream(f), charset); 
        } catch (FileNotFoundException e) { 
                e.printStackTrace(); 
        } 
        return result; 
    }
    
    public static String stream2String(InputStream in, String charset) { 
        StringBuffer sb = new StringBuffer(); 
        try { 
                Reader r = new InputStreamReader(in, charset); 
                int length = 0; 
                for (char[] c = new char[1024]; (length = r.read(c)) != -1;) { 
                        sb.append(c, 0, length); 
                } 
                r.close(); 
        } catch (UnsupportedEncodingException e) { 
                e.printStackTrace(); 
        } catch (FileNotFoundException e) { 
                e.printStackTrace(); 
        } catch (IOException e) { 
                e.printStackTrace(); 
        } 
        return sb.toString(); 
    }
    
	public static String ReqtoString(HttpServletRequest request)
	{
		String data = null;
		ServletInputStream sis;
		try {
			sis = request.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final int BUFFER_SIZE = 1024;
			byte[] buffer = new byte[BUFFER_SIZE];			
			int bLen=0;
			
			while((bLen=sis.read(buffer))>0){
				baos.write(buffer,0,bLen);
			}	
			
		    data=new String(baos.toByteArray(),"UTF-8");	
			
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		return data;
	}
	
	public static Properties getNettyProperties(){
		Properties properties = new Properties();		
		//String fileName = req.getServletContext().getInitParameter("netty-file");
		//InputStream in = CommonUtils.class.getClassLoader().getResourceAsStream(fileName);
		InputStream inputStream = CommonUtils.class.getClassLoader().getResourceAsStream("netty.properties");		
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
	
	//输出消息
	public static void write(String content, HttpServletResponse response)
			throws IOException {
		PrintWriter pw = null;
		try {
			response.setContentType("text/html; charset=UTF-8");
			pw = response.getWriter();			
			pw.write(content);
			pw.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			if (pw != null)
				pw.close();
		}
	}

	public static void  writeXML(String xml, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/xml; charset=UTF-8");
		write(xml, response);
	}

	public static void writeHTML(String html, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		write(html, response);
	}

	public static void writeTEXT(String html, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/plain; charset=UTF-8");
		write(html, response);
	}

	public static void writeJson(Object obj, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/plain; charset=UTF-8");
				
		write(JSONObject.fromObject(obj).toString(), response);
	}
	
    /** 
     * 首字母大写 
     *  
     * @param string 
     * @return 
     */  
    public static String toUpperCase4Index(String string) {  
        char[] methodName = string.toCharArray();  
        methodName[0] = toUpperCase(methodName[0]);  
        return String.valueOf(methodName);  
    }  
      
    /** 
     * 字符转成大写 
     *  
     * @param chars 
     * @return 
     */  
    public static char toUpperCase(char chars) {  
        if (97 <= chars && chars <= 122) {  
            chars ^= 32;  
        }  
        return chars;  
    }
}
