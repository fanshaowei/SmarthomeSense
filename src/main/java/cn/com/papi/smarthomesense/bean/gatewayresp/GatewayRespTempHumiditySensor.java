package cn.com.papi.smarthomesense.bean.gatewayresp;

/**
 * 
 * @author fanshaowei
 *
 *温湿度传感器上报类型消息
 */
public class GatewayRespTempHumiditySensor extends GatewayRespBasic{
	private float temperature;
	private float humidity;
	
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getHumidity() {
		return humidity;
	}
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
		
}
