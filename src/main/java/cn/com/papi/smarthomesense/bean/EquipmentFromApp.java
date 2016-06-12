package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;

public class EquipmentFromApp implements Serializable{
	private static final long serialVersionUID = -4839782019713247292L;
	
	public String equipment_code;
	public String name_device;
	public String num_channel;
	public String name_channel;
	public String getEquipment_code() {
		return equipment_code;
	}
	public void setEquipment_code(String equipment_code) {
		this.equipment_code = equipment_code;
	}
	public String getName_device() {
		return name_device;
	}
	public void setName_device(String name_device) {
		this.name_device = name_device;
	}
	public String getNum_channel() {
		return num_channel;
	}
	public void setNum_channel(String num_channel) {
		this.num_channel = num_channel;
	}
	public String getName_channel() {
		return name_channel;
	}
	public void setName_channel(String name_channel) {
		this.name_channel = name_channel;
	}
}
