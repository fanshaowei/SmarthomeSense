package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;


public class RegDevFromAppBean implements Serializable{

	/**
	 * 手机上传注册信息
	 */
	private static final long serialVersionUID = -5374878940864915990L;
	private String req_token;
	private String username;
	private String terminal_code;
	private int type;
	private EquipmentFromApplistBean equipment_list;
	
	public String getTerminal_code() {
		return terminal_code;
	}
	public void setTerminal_code(String terminal_code) {
		this.terminal_code = terminal_code;
	}
	public EquipmentFromApplistBean getEquipment_list() {
		return equipment_list;
	}
	public void setEquipment_list(EquipmentFromApplistBean equipment_list) {
		this.equipment_list = equipment_list;
	}
	
	public String getReq_token() {
		return req_token;
	}
	
	public void setReq_token(String req_token) {
		this.req_token = req_token;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
