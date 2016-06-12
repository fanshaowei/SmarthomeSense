package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;

public class Equipment implements Serializable{
	/**
	 * 用于提取网关发送的注册设备信息列表，在RegDevToGwBean中用到
	 */
	private static final long serialVersionUID = 1L;
	public String equipment_code;
	public String number;
	
	public Equipment(){
		
	}
	
	public Equipment(String equipment_code,String number){
		this.equipment_code = equipment_code;
		this.number=number;
	}
	
	public String getEquipment_code() {
		return equipment_code;
	}
	public void setEquipment_code(String equipment_code) {
		this.equipment_code = equipment_code;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
}
