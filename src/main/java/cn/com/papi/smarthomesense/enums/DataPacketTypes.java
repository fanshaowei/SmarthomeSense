package cn.com.papi.smarthomesense.enums;

public enum DataPacketTypes {
    
   GATEWAY_LOAD_TO_SERVER(5800),			//家电网关登录到云服务器（NS）
   GATEWAY_REPORT_REG_HOME_DEVICE(5801),	//家电网关上报注册活动执行结束消息
   GATEWAY_REPORT_HOME_DEVICE_MESSAGE(5802),//家电网关上报家电设备的信息
   GATEWAY_REPORT_SCENE_CTL(5803),			//家电网关上报情景控制执行结束消息
   GATEWAY_REPORT_UPDATE_GW_INFO(5804),		//家电网关上报网关配置信息更新完毕消息
   GATEWAY_REPORT_HEARTBEAT(5805),			//家电网关上报心跳ping（T=100s）
   GATEWAY_REPORT_SENSE_DEVICE(5806),		//网关上报报警设备信息
   
   SERVER_TO_GATEWAY_CONFIRM_LOAD(2580),		//云服务器（NS）回复网关登陆确认信息
   SERVER_TO_GATEWAY_REG_DEVICE(2581),		//云服务器发送注册新设备命令
   SERVER_TO_GATEWAY_SINGLE_CTL(2582),		//云服务器发送单控命令
   SERVER_TO_GATEWAY_SCENE_CTL(2583),			//云服务器发送情景控制命令
   SERVER_TO_GATEWAY_DEL_DIVICE_CTL(2584);	//云服务器删除设备命令
   
   private int codeType;
   private DataPacketTypes(int _codeType){
	   this.codeType = _codeType;
   }
   
   public static int getDataPacketType(String name){
	   DataPacketTypes[] gtpts = DataPacketTypes.values();
	   int ct = 0;
	   for(DataPacketTypes gtpt: gtpts){
		   if(name.equals(gtpt.name())){
			   ct =  gtpt.getCodeType();
		   }
	   }
	   return ct;
   }
   
    //get、setter
	public int getCodeType() {
		return codeType;
	}
	public void setCodeType(int codeType) {
		this.codeType = codeType;
	}   
   
}
