package cn.com.papi.smarthomesense.service;

import net.sf.json.JSONObject;

public interface IGatewayMessageService {
	public void parseSenseDeviceAndAction(JSONObject senseDeviceJson);
}
