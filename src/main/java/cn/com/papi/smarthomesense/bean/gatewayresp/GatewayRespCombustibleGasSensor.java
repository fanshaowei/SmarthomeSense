package cn.com.papi.smarthomesense.bean.gatewayresp;

/**
 * 
 * @author fanshaowei
 *
 *可燃气体设备上报消息
 */
public class GatewayRespCombustibleGasSensor extends GatewayRespBasic{

	private float methane;
	private float carbon_monoxide;
	
	public float getMethane() {
		return methane;
	}
	public void setMethane(float methane) {
		this.methane = methane;
	}
	public float getCarbon_monoxide() {
		return carbon_monoxide;
	}
	public void setCarbon_monoxide(float carbon_monoxide) {
		this.carbon_monoxide = carbon_monoxide;
	}
	
	
}
