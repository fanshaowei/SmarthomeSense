package cn.com.papi.common.bean;

import java.io.Serializable;

public class SmarthomeSenseConfig implements Serializable{

	private static final long serialVersionUID = 5501151186330371671L;
	private String smarthomeUrl;

	public String getSmarthomeUrl() {
		return smarthomeUrl;
	}

	public void setSmarthomeUrl(String smarthomeUrl) {
		this.smarthomeUrl = smarthomeUrl;
	}
	
	
}
