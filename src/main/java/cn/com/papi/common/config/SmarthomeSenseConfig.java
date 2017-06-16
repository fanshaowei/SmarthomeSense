package cn.com.papi.common.config;

import java.io.Serializable;

public class SmarthomeSenseConfig implements Serializable{

	private static final long serialVersionUID = 5501151186330371671L;
	private String smarthomeUrl;
	private String smarthomeActive;

	public String getSmarthomeUrl() {
		return smarthomeUrl;
	}

	public void setSmarthomeUrl(String smarthomeUrl) {
		this.smarthomeUrl = smarthomeUrl;
	}

	public String getSmarthomeActive() {
		return smarthomeActive;
	}

	public void setSmarthomeActive(String smarthomeActive) {
		this.smarthomeActive = smarthomeActive;
	}
	
	
}
