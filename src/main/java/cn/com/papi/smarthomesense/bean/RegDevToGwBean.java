package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;
import java.util.ArrayList;


public class RegDevToGwBean implements Serializable{
	/**
	 * 往网关发送注册设备的消息
	 */
	private static final long serialVersionUID = 1L;
	private String terminal_code;
	private int type;
	private EquipmentListBean equipment_list;
	//private ArrayList<Equipment> equipment_list;
	
	public EquipmentListBean getEquipment_list() {
		return equipment_list;
	}
	public void setEquipment_list(EquipmentListBean equipment_list) {
		this.equipment_list = equipment_list;
	}
		
	public String getTerminal_code() {
		return terminal_code;
	}
	/*public ArrayList<Equipment> getEquipment_list() {
		return equipment_list;
	}
	public void setEquipment_list(ArrayList<Equipment> equipment_list) {
		this.equipment_list = equipment_list;
	}*/
	public void setTerminal_code(String terminal_code) {
		this.terminal_code = terminal_code;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
