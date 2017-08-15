package cn.com.papi.smarthomesense.bean.gatewayresp;

/**
 * 
 * @author fanshaowei
 *
 *人体红外感应器上报类型
 */
public class GatewayRespInfraredSensor extends GatewayRespBasic{
	private int state;
	private int illuminance;
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getIlluminance() {
		return illuminance;
	}
	public void setIlluminance(int illuminance) {
		this.illuminance = illuminance;
	}
		
}
