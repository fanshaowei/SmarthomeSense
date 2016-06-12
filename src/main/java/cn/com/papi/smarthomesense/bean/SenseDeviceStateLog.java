package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;
import java.util.Date;

//智能设备上报记录表
public class SenseDeviceStateLog implements Serializable {
	private static final long serialVersionUID = -6765012540105137997L;

	private int id;
	private String idGateway;
	private String idDevice;
	private String deviceType;	
	private Integer stateCode;
	private String stateName;
	private Date time;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdGateway() {
		return idGateway;
	}
	public void setIdGateway(String idGateway) {
		this.idGateway = idGateway;
	}
	public String getIdDevice() {
		return idDevice;
	}
	public void setIdDevice(String idDevice) {
		this.idDevice = idDevice;
	}
	public Integer getStateCode() {
		return stateCode;
	}
	public void setStateCode(Integer stateCode) {
		this.stateCode = stateCode;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}
