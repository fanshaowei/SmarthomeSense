package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;

public class SenseDeviceBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String idGateway;  //网关表id
	private int idFamily;
	private String idDevice;  //设备ID
	private String nameDevice;//设备名称
	private String typeDevice;//设备类型
	private Boolean isActive; //设备是否在线
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNameDevice() {
		return nameDevice;
	}
	public void setNameDevice(String nameDevice) {
		this.nameDevice = nameDevice;
	}
	public String getTypeDevice() {
		return typeDevice;
	}
	public void setTypeDevice(String typeDevice) {
		this.typeDevice = typeDevice;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
	public int getIdFamily() {
		return idFamily;
	}
	public void setIdFamily(int idFamily) {
		this.idFamily = idFamily;
	}
	
}
