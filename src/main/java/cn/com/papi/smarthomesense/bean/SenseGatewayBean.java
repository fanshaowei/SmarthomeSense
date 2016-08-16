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
	private int fid;
	private String createTime;
	private String creater;
	private int creator;
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
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
	}

    	
}
