package cn.com.papi.smarthomesense.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import cn.com.papi.NettyClient.NettyClient;
import cn.com.papi.smarthomesense.bean.Equipment;
import cn.com.papi.smarthomesense.bean.EquipmentFromApp;
import cn.com.papi.smarthomesense.bean.EquipmentListBean;
import cn.com.papi.smarthomesense.bean.RegDevFromAppBean;
import cn.com.papi.smarthomesense.bean.RegDevToGwBean;
import cn.com.papi.smarthomesense.bean.SenseChannelBean;
import cn.com.papi.smarthomesense.bean.SenseDeviceBean;
import cn.com.papi.smarthomesense.bean.SenseGatewayBean;
import cn.com.papi.smarthomesense.enums.DataPacketTypes;
import cn.com.papi.smarthomesense.enums.SenseDeviceType;
import cn.com.papi.smarthomesense.service.INettyControlDevUtil;
import cn.com.papi.smarthomesense.service.IRedisUtilService;
import cn.com.papi.smarthomesense.service.ISenseChannelService;
import cn.com.papi.smarthomesense.service.ISenseDeviceService;
import cn.com.papi.smarthomesense.service.ISenseGatewayService;
import cn.com.papi.smarthomesense.service.IUserAuthenticationService;
import cn.com.papi.smarthomesense.service.IUserService;
import cn.com.papi.smarthomesense.utils.BaseAction;
import cn.com.papi.smarthomesense.utils.CommonUtils;

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
	private ISenseChannelService senseChannelService;
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
		Gson gson = new Gson();
		String reqString = CommonUtils.ReqtoString(request);
		
		String authStr = userAuthenticationService.userNameTokenAuthentication(request, reqString);					
        if(authStr != null){//用户验证
        	writeTEXT(authStr,response);
        }else{
        	RegDevFromAppBean regDevFromAppBean = gson.fromJson(reqString, RegDevFromAppBean.class);
        	final String idGateway = regDevFromAppBean.getTerminal_code();//获取请求中的网关ID
        	
        	/********首先判断网关是否存在*********************************/
			/*List<SenseGatewayBean> senseGateway = isenseGatewayService.getByIdGw(idGateway);
			if(senseGateway.isEmpty()){ 
				writeTEXT(CommonUtils.statusBeanToJson(false, "9999", "网关不存", null), response);
			}*/
			
			/********如果网关存在，把要注册的设备发给长连接服务器*********************************/
			final ArrayList<EquipmentFromApp> equimentFromAppList = 
					(ArrayList<EquipmentFromApp>) regDevFromAppBean.getEquipment_list().getEQUIPMENTLIST();
			
			//设备注册对象
			RegDevToGwBean regDevToGwbean = new RegDevToGwBean();	
			
			EquipmentListBean equipmentListBean = new EquipmentListBean();
			List<Equipment> equipmentList = new ArrayList<Equipment>();				
			for(EquipmentFromApp qfa : equimentFromAppList){
				Equipment equipment = new Equipment();
				equipment.setEquipment_code(qfa.getEquipment_code());
				equipment.setNumber(qfa.getNum_channel());
				
				equipmentList.add(equipment);				
			}
			equipmentListBean.setEQUIPMENTLIST(equipmentList);
			regDevToGwbean.setEquipment_list(equipmentListBean);
			regDevToGwbean.setType(2);
			regDevToGwbean.setTerminal_code(idGateway);								
			String data = gson.toJson(regDevToGwbean);				 																									
			//将要注册的设备信息发给nettyServer
			nettyControlDevUtil.NettySendMsg(client, DataPacketTypes.SERVER_TO_GATEWAY_REG_DEVICE.getCodeType(), idGateway, data);
			
			/********启用异步容器，设备在网关注册成功后，再将要添加的设备添加到数据库*********************************/
		    final String returnStr;
			//异步添加智能输入设备到数据库
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
						List<SenseDeviceBean> getSenseDevice = new ArrayList<SenseDeviceBean>();																			
						//List<EquipmentFromApp> equipmentsFromApp = equimentFromAppList.getEQUIPMENTLIST();//获取上传的设备列表具体信息
						PrintWriter acPrinter = ac.getResponse().getWriter();
													
						//判断网关是否存在
						List<SenseGatewayBean> senseGateway = null;
						try {
							senseGateway = isenseGatewayService.getByIdGw(idGateway);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						if(senseGateway.isEmpty()){ 
							//网关不存在
							acPrinter.println(CommonUtils.statusBeanToJson(false, "5997", "该网关不存在", null));						
							acPrinter.flush();
						}else{
							//判断设备是否存在
							for(EquipmentFromApp equipmentFromApp: equimentFromAppList){																													
								//判断设备是否已经存在
								String idDevice = equipmentFromApp.getEquipment_code();//设备id
								String nameDevice = equipmentFromApp.getName_device();//设备名
								String idChannel = equipmentFromApp.getNum_channel();//设备通道
								String nameChannel = equipmentFromApp.getName_channel();//通道名
								
								try {
									getSenseDevice = senseDeviceService.getListByIdDevice(idDevice);
								} catch (Exception e) {
									e.printStackTrace();
								}									
								if(!getSenseDevice.isEmpty()){//已经存在的设备																			
									acPrinter.println(CommonUtils.statusBeanToJson(false, "5998", "设备已经存在，无需重复添加", null));
									acPrinter.flush();
								}else{
									//设备不存在,添加										
									SenseDeviceBean senseDeviceBean = new SenseDeviceBean();
									senseDeviceBean.setIdDevice(idDevice);
									senseDeviceBean.setIdGateway(idGateway);
									senseDeviceBean.setNameDevice(nameDevice);
									senseDeviceBean.setIsActive(false);										
									//获取设备类型
									String deviceType = SenseDeviceType.getDeviceTypeName(CommonUtils.subDeviceTypeCode(idDevice));										
									senseDeviceBean.setTypeDevice(deviceType);									
									//设备入库										
									try {
										@SuppressWarnings("unused")
										int resultAddDev = senseDeviceService.add(senseDeviceBean);
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									//添加通道表
									List<SenseChannelBean> getSenseChannel = null;
									try {
										getSenseChannel = senseChannelService.getByDeviceAndChannel(idDevice, idChannel);
									} catch (Exception e) {
										e.printStackTrace();
									}
									if(getSenseChannel.isEmpty()){
										SenseChannelBean senseChannelBean = new SenseChannelBean();
										senseChannelBean.setIdChannel(idChannel);//通道id										
									    senseChannelBean.setStatus(0);		//通道状态											    
									    senseChannelBean.setNameChannel(nameChannel);//通道名
										senseChannelBean.setIdDevice(idDevice);//设备id																												
										
										Gson controlJson = new Gson();
										Map<String,Object> controlMap = new HashMap<String,Object>();
										controlMap.put("terminal_code", idGateway);
										controlMap.put("equipment_code", idDevice);										
										controlMap.put("num", idChannel);
										controlMap.put("type", 2);
										controlMap.put("state", 1);
																		
										senseChannelBean.setControlJson(controlJson.toJson(controlMap));									
										//设备通道入库
										try {
											@SuppressWarnings("unused")
											int resultAddChannel = senseChannelService.add(senseChannelBean);
										} catch (Exception e) {
											e.printStackTrace();
										}
										
										acPrinter.println(CommonUtils.statusBeanToJson(true, "0000", "添加设备成功！", null));
										acPrinter.flush();
									}else{
										acPrinter.println(CommonUtils.statusBeanToJson(false, "5999", "设通道已经存在，无需重复添加", null));
										acPrinter.flush();
									}//添加通道表结束							
								}//添加设备结束																																			
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
        }        
								
	}		
 
	/**
	 * 删除设备，从网关删除注册信息，并异步删除数据库的数据 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="deleteDevice",method=RequestMethod.POST)
	public void deleteDevice(HttpServletRequest request, HttpServletResponse response){
		String reqString = CommonUtils.ReqtoString(request);
		//验证用户是否全安
		//java反射调用
/*		IUserAuthenticationService iUserAuthenticationService = UserAuthenticationFactory.getUserAuthenticationFactory();
		String authStr = iUserAuthenticationService.userNameTokenAuthentication(request, reqString);
		//cglib调用
		UserAuthenticationServiceImpl userAuthenticationInterfaceImpl = 
				(UserAuthenticationServiceImpl) CglibProxyFactory.getCglibProxyObj(UserAuthenticationServiceImpl.class.getName());
		String authStr1 =  
		    userAuthenticationInterfaceImpl.userNameTokenAuthentication(request, reqString);*/
		
		String authStr = userAuthenticationService.userNameTokenAuthentication(request, reqString);			
		
        if(authStr != null){
        	writeTEXT(authStr,response);
        }else{        
			JSONObject json = JSONObject.fromObject(reqString);						
            final String idDevice = json.getString("idDevice");
			int type = 2; 
			String data = null;
			String idGateway = null;
						
			try {
				//判断设备是否存在
				SenseDeviceBean senseDevice = senseDeviceService.getSenseDeviceByIdDevice(idDevice);
				if (senseDevice != null){//设备存在
				    idGateway = senseDevice.getIdGateway();
				    
				    //判断设备通道是否存在
					List<SenseChannelBean> senseChannelBeanList = senseChannelService.getByIdDevice(idDevice);					
					if(senseChannelBeanList != null){//设备通道存在
						RegDevToGwBean regDevToGwBean = new RegDevToGwBean();
						EquipmentListBean equipmentListBean = new EquipmentListBean();
						List<Equipment> equipmentList = new ArrayList<Equipment>();
						for(SenseChannelBean scb: senseChannelBeanList){
							Equipment equipment = new Equipment();
							equipment.setNumber(scb.getIdChannel());
							equipment.setEquipment_code(idDevice);
							
							equipmentList.add(equipment);
						}
						equipmentListBean.setEQUIPMENTLIST(equipmentList);
						regDevToGwBean.setEquipment_list(equipmentListBean);
						regDevToGwBean.setTerminal_code(idGateway);
						regDevToGwBean.setType(type);
						
						Gson gson = new Gson();
					    data = gson.toJson(regDevToGwBean);					  					  
					}//end if 										
					
					//发送信息到网关删除设备
					nettyControlDevUtil.NettySendMsg(client, DataPacketTypes.SERVER_TO_GATEWAY_DEL_DIVICE_CTL.getCodeType(), idGateway, data);
				}//end if

				
				/*****************启用异步容器*************************************************/
				response.setContentType("text/html;charset=UTF-8");
				response.setHeader("Cash-Control", "private");
				response.setHeader("Pragma", "no-cache");
				request.setCharacterEncoding("UTF-8");
				request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED",	 true);
				final AsyncContext ac = request.startAsync();
				ac.setTimeout(30 * 1000);
				new Work(ac).start();
				
				final String keyAsyncResp = "del_"+idGateway;//将网关ID作为传递异步response的键值
				ServletHashMap.ASYNC_CONTEXT_DELETE.put(keyAsyncResp, ac);
				ac.addListener(new AsyncListener(){

					@Override
					public void onComplete(AsyncEvent arg0) throws IOException {
						
						if(ServletHashMap.ASYNC_CONTEXT_DELETE.containsKey(keyAsyncResp)){							
							try {
								//删除数据库数据设备通道
								Integer delChannelNum = senseChannelService.deleteByIdDevice(idDevice);
								//删除数据库数据 设备
								Integer delDeviceNum = senseDeviceService.deleteByIdDevice(idDevice);
							} catch (Exception e) {								
								e.printStackTrace();
							}							
						}
						
						arg0.getSuppliedResponse().getWriter().close();
						ServletHashMap.ASYNC_CONTEXT_DELETE.remove(keyAsyncResp);
						if(null==ServletHashMap.ASYNC_CONTEXT_DELETE.get(keyAsyncResp)){
							System.out.println("该值已经删除，当前存储的删除设备异步请求数：" + ServletHashMap.ASYNC_CONTEXT_DELETE.size());
						}
						System.out.println("异步结束");
						
					}

					@Override
					public void onError(AsyncEvent arg0) throws IOException {
						System.out.println("删除设备异步错误");						
					}

					@Override
					public void onStartAsync(AsyncEvent arg0)
							throws IOException {
						System.out.println("删除设备异步开始");						
					}

					@Override
					public void onTimeout(AsyncEvent arg0) throws IOException {
						System.out.println("超时了");
						if(ServletHashMap.ASYNC_CONTEXT_DELETE.containsKey(keyAsyncResp)){
							AsyncContext ac = 
									ServletHashMap.ASYNC_CONTEXT_DELETE.get(keyAsyncResp);
							PrintWriter acPrinter = ac.getResponse().getWriter();
							String deviceState = CommonUtils.statusBeanToJson(false, "7000", "链接超时", null);
							acPrinter.print(deviceState);
							acPrinter.flush();
							
							if(ServletHashMap.ASYNC_CONTEXT_DELETE.containsKey(keyAsyncResp)){
								ServletHashMap.ASYNC_CONTEXT_DELETE.remove(keyAsyncResp);  //释放异步响应资源
								if(null==ServletHashMap.ASYNC_CONTEXT_DELETE.get(keyAsyncResp))
									System.out.println("该值已经删除");
							}
						}						
					}
					
				});				
			} catch (Exception e) {				
				e.printStackTrace();
			}//end try
        }//end if

	}//end deleteDevice
	
}
