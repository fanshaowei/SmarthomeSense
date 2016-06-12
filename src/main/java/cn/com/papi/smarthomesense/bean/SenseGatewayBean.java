package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;

public class SenseGatewayBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9094597760930611922L;
    
	private int uuid;
	private String idGateway;
	private int type;
	private String nameGateway;
	private int idFamily;
	private String createTime;
	private int creater;
	private String creatername;
	public int getUuid() {
		return uuid;
	}
	public void setUuid(int uuid) {
		this.uuid = uuid;
	}
	public String getIdGateway() {
		return idGateway;
	}
	public void setIdGateway(String idGateway) {
		this.idGateway = idGateway;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getNameGateway() {
		return nameGateway;
	}
	public void setNameGateway(String nameGateway) {
		this.nameGateway = nameGateway;
	}
	public int getIdFamily() {
		return idFamily;
	}
	public void setIdFamily(int idFamily) {
		this.idFamily = idFamily;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getCreater() {
		return creater;
	}
	public void setCreater(int creater) {
		this.creater = creater;
	}
	public String getCreatername() {
		return creatername;
	}
	public void setCreatername(String creatername) {
		this.creatername = creatername;
	}
	
	
}
