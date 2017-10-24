package cn.com.papi.smarthomesense.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.papi.NettyClient.NettyClient;
import cn.com.papi.smarthomesense.bean.Equipment;
import cn.com.papi.smarthomesense.bean.EquipmentFromApp;
import cn.com.papi.smarthomesense.bean.EquipmentListBean;
import cn.com.papi.smarthomesense.bean.RegDevFromAppBean;
import cn.com.papi.smarthomesense.bean.RegDevToGwBean;
import cn.com.papi.smarthomesense.bean.SenseDeviceBean;
import cn.com.papi.smarthomesense.bean.SenseGatewayBean;
import cn.com.papi.smarthomesense.enums.DataPacketTypes;
import cn.com.papi.smarthomesense.enums.SenseDeviceType;
import cn.com.papi.smarthomesense.service.INettyControlDevUtil;
import cn.com.papi.smarthomesense.service.IRedisUtilService;
import cn.com.papi.smarthomesense.service.ISenseDeviceService;
import cn.com.papi.smarthomesense.service.ISenseGatewayService;
import cn.com.papi.smarthomesense.service.IUserAuthenticationService;
import cn.com.papi.smarthomesense.service.IUserService;
import cn.com.papi.smarthomesense.utils.BaseAction;
import cn.com.papi.smarthomesense.utils.CommonUtils;

import com.google.gson.Gson;

/**
 * 
 * @author fanshaowei
 *  
 * 该类用于处理向网关注册设备的请求。 
 */
@Controller
public class NettyClientAction extends BaseAction{	
	private static String getIP = "";
	private static String getPORT = "";
	
	public static NettyClient client ;
	@Resource
	private INettyControlDevUtil nettyControlDevUtil;	
	@Resource
	private IUserService iUserService;	
	@Resource
	private IRedisUtilService iRedisUtilService;	
	@Resource
	private ISenseDeviceService senseDeviceService;	
	@Resource
	private ISenseGatewayService isenseGatewayService;	
	@Resource
	private IUserAuthenticationService userAuthenticationService;
	
	static{
		Properties properties = CommonUtils.getNettyProperties();		
		getIP = properties.getProperty("netty.host");
		getPORT = properties.getProperty("netty.port");	
		client = new NettyClient(getIP,getPORT);
		Thread thread = new Thread(){
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				super.run();
				client.connect();
			}
		};	
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * 添加设备，注册设备到网关，并异步添加数据到数据库
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/addDevice",method=RequestMethod.POST)
	public void addDevice(HttpServletRequest request, HttpServletResponse response) throws Exception{		
		
		/********获取请求的数据********/
		Gson gson = new Gson();
		String reqString = CommonUtils.ReqtoString(request);
		
		/********验证用户是否安全********/
		String authStr = userAuthenticationService.userNameTokenAuthentication(request, reqString);					
        if(authStr != null){//用户验证
        	writeTEXT(authStr,response);
        }else{
        	RegDevFromAppBean regDevFromAppBean = gson.fromJson(reqString, RegDevFromAppBean.class);
        	final String idGateway = regDevFromAppBean.getTerminal_code();//获取请求中的网关ID
        	
        	/********首先判断网关是否存在*********************************/
			List<SenseGatewayBean> senseGateway = isenseGatewayService.getByIdGw(idGateway);
			if(senseGateway.isEmpty()){ 
				writeTEXT(CommonUtils.statusBeanToJson(false, "5997", "网关不存", null), response);
			}else{
			
				/********如果网关存在，把要注册的设备发给长连接服务器*********************************/
				final ArrayList<EquipmentFromApp> equimentFromAppList = 
						(ArrayList<EquipmentFromApp>) regDevFromAppBean.getEquipment_list().getEQUIPMENTLIST();
				
				//设备注册对象
				RegDevToGwBean regDevToGwbean = new RegDevToGwBean();	
				
				EquipmentListBean equipmentListBean = new EquipmentListBean();
				List<Equipment> equipmentList = new ArrayList<Equipment>();				
				for(EquipmentFromApp qfa : equimentFromAppList){
					/****************判断设备是否存在******************************/
					boolean isSenseDeviceExit = senseDeviceService.isSenseDeviceExit(qfa.getEquipment_code());
					if(isSenseDeviceExit){
						writeTEXT(CommonUtils.statusBeanToJson(false, "5998", "设备已经存在，无需重复添加", qfa.getEquipment_code()),response);
						break;
					}
					
					Equipment equipment = new Equipment();
					equipment.setEquipment_code(qfa.getEquipment_code());
					equipment.setNumber(qfa.getNum_channel());
					
					equipmentList.add(equipment);				
				}
				
				/****************要注册的设备对象不为空,就向网关推送注册信息******************************/
				if(equipmentList != null && equipmentList.size() > 0){
									
					equipmentListBean.setEQUIPMENTLIST(equipmentList);
					regDevToGwbean.setEquipment_list(equipmentListBean);
					regDevToGwbean.setType(2);
					regDevToGwbean.setTerminal_code(idGateway);								
					String data = gson.toJson(regDevToGwbean);				 																									
					//将要注册的设备信息发给nettyServer
					nettyControlDevUtil.NettySendMsg(client, DataPacketTypes.SERVER_TO_GATEWAY_REG_DEVICE.getCodeType(), idGateway, data);
					
					/********启用异步容器，设备在网关注册成功后，再将要添加的设备添加到数据库*********************************/
					response.setContentType("text/html;charset=UTF-8");
					response.setHeader("Cash-Control", "private");
					response.setHeader("Pragma", "no-cache");
					request.setCharacterEncoding("UTF-8");
					request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED",	 true);
					final AsyncContext ac = request.startAsync();
					ac.setTimeout(30 * 1000);
					new Work(ac).start();
					
					final String keyAsyncResp = idGateway;//将网关ID作为传递异步response的键值
					ServletHashMap.ASYNC_CONTEXT_REGDEV.put(keyAsyncResp, ac);
					ac.addListener(new AsyncListener(){
						@Override
						public void onComplete(AsyncEvent arg0) throws IOException {
							if(ServletHashMap.ASYNC_CONTEXT_REGDEV.containsKey(keyAsyncResp)){
								//List<SenseDeviceBean> getSenseDevice = new ArrayList<SenseDeviceBean>();
								PrintWriter acPrinter = ac.getResponse().getWriter();
															
								/********判断网关是否存在********/
								List<SenseGatewayBean> senseGateway = null;
								try {
									senseGateway = isenseGatewayService.getByIdGw(idGateway);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								if(senseGateway == null || senseGateway.isEmpty()){ 
									acPrinter.println(CommonUtils.statusBeanToJson(false, "5997", "该网关不存在", idGateway));						
									acPrinter.flush();
								}else{
									/********判断设备是否存在********/
									for(EquipmentFromApp equipmentFromApp: equimentFromAppList){
										String idDevice = equipmentFromApp.getEquipment_code();//设备id
										String nameDevice = equipmentFromApp.getName_device();//设备名
										String deviceParam = JSONObject.fromObject(equipmentFromApp.getDeviceParam()).toString();						
										/*try {
											getSenseDevice = senseDeviceService.getListByIdDevice(idDevice);
										} catch (Exception e) {
											e.printStackTrace();
										}									
										if(!getSenseDevice.isEmpty()){
											*//********设备已经存在,返回设备存在消息********//*																			
											acPrinter.println(CommonUtils.statusBeanToJson(false, "5998", "设备已经存在，无需重复添加", idDevice));
											acPrinter.flush();
											break;
										}else{*/
											/********设备不存在,添加设备到数据库********/										
											SenseDeviceBean senseDeviceBean = new SenseDeviceBean();
											senseDeviceBean.setIdDevice(idDevice);
											senseDeviceBean.setIdGateway(idGateway);
											senseDeviceBean.setNameDevice(nameDevice);
											senseDeviceBean.setIsActive(true);																				
											String deviceType = SenseDeviceType.getDeviceTypeName(CommonUtils.subDeviceTypeCode(idDevice));	//获取设备类型									
											senseDeviceBean.setTypeDevice(deviceType);								
											senseDeviceBean.setDeviceParam(deviceParam);
											
											try {//根据网关id获取家庭id
												List<SenseGatewayBean> senseGatewayList = isenseGatewayService.getGatewayFamily(idGateway);
												int idFamily = senseGatewayList.get(0).getFid();
												senseDeviceBean.setIdFamily(idFamily);
											} catch (Exception e1) {
												e1.printStackTrace();
											}
											
											try {
												int resultAddDev = senseDeviceService.add(senseDeviceBean);//设备入库	
												
												if(resultAddDev>0){
													acPrinter.println(CommonUtils.statusBeanToJson(true, "0000", "添加设备成功！", senseDeviceBean));
													acPrinter.flush();
												}else{
													acPrinter.println(CommonUtils.statusBeanToJson(true, "5996", "添加设备失败！", senseDeviceBean));
													acPrinter.flush();
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
																										
										//}//添加设备结束																																			
								    }//end for
								}									     						
								
								arg0.getSuppliedResponse().getWriter().close();
								ServletHashMap.ASYNC_CONTEXT_REGDEV.remove(keyAsyncResp);
								if(null==ServletHashMap.ASYNC_CONTEXT_REGDEV.get(keyAsyncResp)){
									System.out.println("该值已经删除，当前存储的注册异步请求数：" + ServletHashMap.ASYNC_CONTEXT_REGDEV.size());
								}
								System.out.println("异步结束");
							}						
						}
		
						@Override
						public void onError(AsyncEvent arg0) throws IOException {
							System.out.println("异步错误");						
						}//end onError
		
						@Override
						public void onStartAsync(AsyncEvent arg0)
								throws IOException {					
							System.out.println("开始异步");
						}//end onStartAsync
		
						@Override 
						public void onTimeout(AsyncEvent arg0) throws IOException {						
							System.out.println("超时了");
							if(ServletHashMap.ASYNC_CONTEXT_REGDEV.containsKey(keyAsyncResp)){
								AsyncContext ac = 
										ServletHashMap.ASYNC_CONTEXT_REGDEV.get(keyAsyncResp);
								PrintWriter acPrinter = ac.getResponse().getWriter();
								String deviceState = CommonUtils.statusBeanToJson(false, "7000", "链接超时", null);
								acPrinter.print(deviceState);
								acPrinter.flush();
								
								if(ServletHashMap.ASYNC_CONTEXT_REGDEV.containsKey(keyAsyncResp)){
									ServletHashMap.ASYNC_CONTEXT_REGDEV.remove(keyAsyncResp);  //释放异步响应资源
									if(null==ServletHashMap.ASYNC_CONTEXT_REGDEV.get(keyAsyncResp))
										System.out.println("该值已经删除");
								}
							}					
						}//end onTimeout
						
					});	//end async context
				}//注册设备结束
			}//判断网关是否存在
        }//判断用户是否合法       
								
	}		
 
	/**
	 * 删除设备，从网关删除注册信息，并异步删除数据库的数据 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="deleteDevice",method=RequestMethod.POST)
	public void deleteDevice(HttpServletRequest request, HttpServletResponse response){		
		//java反射调用
/*		IUserAuthenticationService iUserAuthenticationService = UserAuthenticationFactory.getUserAuthenticationFactory();
		String authStr = iUserAuthenticationService.userNameTokenAuthentication(request, reqString);
		//cglib调用
		UserAuthenticationServiceImpl userAuthenticationInterfaceImpl = 
				(UserAuthenticationServiceImpl) CglibProxyFactory.getCglibProxyObj(UserAuthenticationServiceImpl.class.getName());
		String authStr1 =  
		    userAuthenticationInterfaceImpl.userNameTokenAuthentication(request, reqString);*/
		
		/********获取请求的数据********/
		String reqString = CommonUtils.ReqtoString(request);
		JSONObject json = JSONObject.fromObject(reqString);
		final JSONArray idDeviceArray = json.getJSONArray("idDevice");
		String idGateway = json.getString("terminal_code");
		/********验证用户是否安全********/
		String authStr = userAuthenticationService.userNameTokenAuthentication(request, reqString);			
		
        if(authStr != null){
        	/********用户不存在，则返回用户不存在消息********/
        	writeTEXT(authStr,response);
        }else{  
        	/********首先判断网关是否存在********/
			List<SenseGatewayBean> senseGateway = null;
			try {
				senseGateway = isenseGatewayService.getByIdGw(idGateway);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(senseGateway!=null && senseGateway.isEmpty()){ 
				/*********网关不存在，则返回网关不存在消息********/
				writeTEXT(CommonUtils.statusBeanToJson(false, "5997", "网关不存在", null), response);				
			}else{				        	
				String idDevice = "";//设备id
				int type = 2; //设备类型字
				String sendNettyData = null;//发送给网关的数据			
				
				RegDevToGwBean regDevToGwBean = new RegDevToGwBean();	
				EquipmentListBean equipmentListBean = new EquipmentListBean();
				List<Equipment> equipmentList = new ArrayList<Equipment>();
				try {
					for(int i=0; i<idDeviceArray.size(); i++ ){
						idDevice = idDeviceArray.getString(i);
						/********判断要删除的设备是否存在数据库********/
						boolean isSenseDeviceExit = senseDeviceService.isSenseDeviceExit(idDevice);																	
						if (isSenseDeviceExit){//设备存在											    
							Equipment equipment = new Equipment();						
							equipment.setNumber("1");
							equipment.setEquipment_code(idDevice);		
							
							equipmentList.add(equipment);						
						}else{
							writeTEXT(CommonUtils.statusBeanToJson(true, "3001", "设备不存在", null),response);
							break;
						}//end if					
					}//end idDeviceArray for
					/********判断拼凑的要删除的设备列表是否为空********/
					if(equipmentList != null && equipmentList.size()>0){
						equipmentListBean.setEQUIPMENTLIST(equipmentList);					
						regDevToGwBean.setEquipment_list(equipmentListBean);
						regDevToGwBean.setTerminal_code(idGateway);
						regDevToGwBean.setType(type);									
					
						Gson gson = new Gson();
						sendNettyData = gson.toJson(regDevToGwBean);
						//发送信息到网关删除设备
						nettyControlDevUtil.NettySendMsg(client, DataPacketTypes.SERVER_TO_GATEWAY_DEL_DIVICE_CTL.getCodeType(), idGateway, sendNettyData);
						
						/*****************启用异步容器*************************************************/
						response.setContentType("text/html;charset=UTF-8");
						response.setHeader("Cash-Control", "private");
						response.setHeader("Pragma", "no-cache");
						request.setCharacterEncoding("UTF-8");
						request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED",	 true);
						final AsyncContext ac = request.startAsync();
						ac.setTimeout(30 * 1000);
						new Work(ac).start();
						
						final String keyAsyncResp = idGateway;//将网关ID作为传递异步response的键值
						ServletHashMap.ASYNC_CONTEXT_DELETE.put(keyAsyncResp, ac);
						ac.addListener(new AsyncListener(){
	
							@Override
							public void onComplete(AsyncEvent arg0) throws IOException {
								PrintWriter acPrinter = ac.getResponse().getWriter();
								
								if(ServletHashMap.ASYNC_CONTEXT_DELETE.containsKey(keyAsyncResp)){							
									try {
										/********删除数据库数据 设备********/
										Integer delCnt = 0;
										for(int i= 0 ;i<idDeviceArray.size(); i++){
											String idDevice = idDeviceArray.getString(i);										
											senseDeviceService.deleteByIdDevice(idDevice);
											delCnt ++;								
										}
										
										if(delCnt == idDeviceArray.size()){
											String deviceState = CommonUtils.statusBeanToJson(true, "3000", "删除设备成功", null);
											acPrinter.println(deviceState);
											acPrinter.flush();
										}else{
											String deviceState = CommonUtils.statusBeanToJson(false, "3999", "删除设备失败", null);
											acPrinter.println(deviceState);
											acPrinter.flush();
										}
										
										System.out.print("-------------------异步删除设备数据成功------------------");
									} catch (Exception e) {								
										e.printStackTrace();
									}							
								}
								
								arg0.getSuppliedResponse().getWriter().close();
								ServletHashMap.ASYNC_CONTEXT_DELETE.remove(keyAsyncResp);
								if(null==ServletHashMap.ASYNC_CONTEXT_DELETE.get(keyAsyncResp)){
									System.out.println("该值已经删除，当前存储的删除设备异步请求数：" + ServletHashMap.ASYNC_CONTEXT_DELETE.size());
								}
								System.out.println("---------------异步结束---------------------");
								
							}
	
							@Override
							public void onError(AsyncEvent arg0) throws IOException {
								System.out.println("------------------删除设备异步错误----------------");						
							}
	
							@Override
							public void onStartAsync(AsyncEvent arg0)
									throws IOException {
								System.out.println("-----------------删除设备异步开始----------------");						
							}
	
							@Override
							public void onTimeout(AsyncEvent arg0) throws IOException {
								System.out.println("超时了");
								if(ServletHashMap.ASYNC_CONTEXT_DELETE.containsKey(keyAsyncResp)){
									AsyncContext ac = 
											ServletHashMap.ASYNC_CONTEXT_DELETE.get(keyAsyncResp);
									PrintWriter acPrinter = ac.getResponse().getWriter();
									String deviceState = CommonUtils.statusBeanToJson(false, "7000", "链接超时", null);
									acPrinter.println(deviceState);
									acPrinter.flush();
									
									if(ServletHashMap.ASYNC_CONTEXT_DELETE.containsKey(keyAsyncResp)){
										ServletHashMap.ASYNC_CONTEXT_DELETE.remove(keyAsyncResp);  //释放异步响应资源
										if(null==ServletHashMap.ASYNC_CONTEXT_DELETE.get(keyAsyncResp))
											System.out.println("------------------该值已经删除--------------------");
									}
								}						
							}
							
						});	
				 }												
				} catch (Exception e) {				
					e.printStackTrace();
				}//end try
		    }//end if 网关是否在
        }//end if 用户是否存在

	}//end deleteDevice
	
}
