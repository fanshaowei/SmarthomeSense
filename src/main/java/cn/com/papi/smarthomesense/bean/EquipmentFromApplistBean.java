package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;
import java.util.List;

public class EquipmentFromApplistBean implements Serializable{

	private static final long serialVersionUID = -313863681702261135L;
	private List<EquipmentFromApp> EQUIPMENTLIST;
	
	public EquipmentFromApplistBean(){}
	
	public EquipmentFromApplistBean(List<EquipmentFromApp> EQUIPMENTLIST){
		this.setEQUIPMENTLIST(EQUIPMENTLIST);
	}

	public List<EquipmentFromApp> getEQUIPMENTLIST() {
		return EQUIPMENTLIST;
	}

	public void setEQUIPMENTLIST(List<EquipmentFromApp> eQUIPMENTLIST) {
		EQUIPMENTLIST = eQUIPMENTLIST;
	}
}
