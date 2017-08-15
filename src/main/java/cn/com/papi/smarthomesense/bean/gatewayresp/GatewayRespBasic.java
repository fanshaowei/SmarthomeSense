package cn.com.papi.smarthomesense.bean.gatewayresp;

/**
 * 
 * @author fanshaowei
 *
 *网关上报消息基础类
 *terminal_code 网关id
 *type 类别标识，区分是 1是输出设备，2是输入设备
 *equipment_code 设备id
 *number 设备标识号，类似设备通道channel
 */
public class GatewayRespBasic {
	private String terminal_code;
	private int type;
	private String equipment_code;
	private int number;
	
	public String getTerminal_code() {
		return terminal_code;
	}
	public void setTerminal_code(String terminal_code) {
		this.terminal_code = terminal_code;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getEquipment_code() {
		return equipment_code;
	}
	public void setEquipment_code(String equipment_code) {
		this.equipment_code = equipment_code;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
		
}
