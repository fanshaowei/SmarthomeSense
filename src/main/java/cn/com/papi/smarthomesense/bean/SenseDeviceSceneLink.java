package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;

//智能设备与情景联动关系表
public class SenseDeviceSceneLink implements Serializable {

	private static final long serialVersionUID = -3546043272877391936L;
    
	private int id;
	private String idGateway;
	private String idDevice;
	private int idChannel;
	private int idScene;
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
	public int getIdChannel() {
		return idChannel;
	}
	public void setIdChannel(int idChannel) {
		this.idChannel = idChannel;
	}
	public int getIdScene() {
		return idScene;
	}
	public void setIdScene(int idScene) {
		this.idScene = idScene;
	}
	
	
}
