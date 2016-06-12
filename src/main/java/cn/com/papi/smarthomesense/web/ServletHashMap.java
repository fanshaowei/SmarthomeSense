package cn.com.papi.smarthomesense.web;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.AsyncContext;

import org.apache.http.client.methods.HttpGet;

public class ServletHashMap {

	public static final ConcurrentHashMap<String, AsyncContext> ASYNC_CONTEXT = new ConcurrentHashMap<String, AsyncContext>();
	public static final ConcurrentHashMap<String, AsyncContext> ASYNC_CONTEXT_REGDEV = new ConcurrentHashMap<String,AsyncContext>();
	public static final ConcurrentHashMap<String, AsyncContext> ASYNC_CONTEXT_SCENECTR = new ConcurrentHashMap<String, AsyncContext>();
	public static final ConcurrentHashMap<String, AsyncContext> ASYNC_CONTEXT_DELETE = new ConcurrentHashMap<String, AsyncContext>();
	public static final HashMap<String, String> VERIFY_CONTEXT = new HashMap<String,String>();
	
	public static final ConcurrentHashMap<String,HttpGet[]> HTTP_GET_REQUEST_SCENE = new ConcurrentHashMap<String,HttpGet[]>();
}
