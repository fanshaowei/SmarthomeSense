package cn.com.papi.smarthomesense.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class BaseAction {
	public Logger log;

	public BaseAction() {
		log = Logger.getLogger(getClass());
	}

	public void write(String content, HttpServletResponse response){
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(content);
			pw.flush();
		} catch (IOException e) {
			log.debug(e.getMessage(), e);
			try {
				throw e;
			} catch (IOException e1) {				
				e1.printStackTrace();
			}
		} finally {
			if (pw != null)
				pw.close();
		}
	}

	public void writeXML(String xml, HttpServletResponse response){
		response.setContentType("text/xml; charset=UTF-8");
		write(xml, response);
	}

	public void writeHTML(String html, HttpServletResponse response){
		response.setContentType("text/html; charset=UTF-8");
		write(html, response);
	}

	public void writeTEXT(String html, HttpServletResponse response){
		response.setContentType("text/plain; charset=UTF-8");
		write(html, response);
	}

	public void writeJson(Object obj, HttpServletResponse response){
		response.setContentType("text/plain; charset=UTF-8");
				
		write(JSONObject.fromObject(obj).toString(), response);
	}
}
