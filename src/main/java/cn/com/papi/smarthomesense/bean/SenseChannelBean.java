package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;

public class SenseChannelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7423200011728476203L;

	private int id;
	private String idChannel;
	private String nameChannel;
	private int status;
	private int idGroup;   //设备通道分组ID
	private String controlJson;
	private String idDevice;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdChannel() {
		return idChannel;
	}
	public void setIdChannel(String idChannel) {
		this.idChannel = idChannel;
	}
	public String getNameChannel() {
		return nameChannel;
	}
	public void setNameChannel(String nameChannel) {
		this.nameChannel = nameChannel;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIdGroup() {
		return idGroup;
	}
	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}
	public String getControlJson() {
		return controlJson;
	}
	public void setControlJson(String controlJson) {
		this.controlJson = controlJson;
	}
	public String getIdDevice() {
		return idDevice;
	}
	public void setIdDevice(String idDevice) {
		this.idDevice = idDevice;
	}
}
