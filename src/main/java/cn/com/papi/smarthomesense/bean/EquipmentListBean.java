package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;
import java.util.List;

public class EquipmentListBean implements Serializable {

	private static final long serialVersionUID = 3624482253420867747L;
	private List<Equipment> EQUIPMENTLIST;
	
	public EquipmentListBean(){}
	
	public EquipmentListBean(List<Equipment> EQUIPMENTLIST){
		this.setEQUIPMENTLIST(EQUIPMENTLIST);
	}

	public List<Equipment> getEQUIPMENTLIST() {
		return EQUIPMENTLIST;
	}

	public void setEQUIPMENTLIST(List<Equipment> eQUIPMENTLIST) {
		EQUIPMENTLIST = eQUIPMENTLIST;
	}
}
