package cn.com.papi.smarthomesense.enums;

public enum SenseDeviceState {
    REPORT("设备报到",0),
    NORMAL_ALARM("普通报警",1),
    DISMANTLE_ALARM("拆动报警",2),
    UNDERVOLTAGE_ALARM("欠压报警",3),
    OPERATTE_FAIL("操作失败",254),
    DEVICE_OFF_LINE("设备离线",255);
    
    private String name;
    private int code;
    private SenseDeviceState(String _name, int _code){
    	this.name = _name;
    	this.code = _code;
    }
    
    public static String getSenseDeviceState(int _code){
    	SenseDeviceState[] sds = SenseDeviceState.values();
 	   String stateName = null;
 	   for(SenseDeviceState state: sds){
 		   if(_code == state.getCode()){
 			  stateName =  state.getName();
 		   }
 	   }
 	   return stateName;
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}        
    

}
