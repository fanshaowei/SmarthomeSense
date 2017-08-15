package cn.com.papi.smarthomesense.enums;

/**
 * 设备名字及设备编码
 * @author fanshaowei
 *
 */
public enum SenseDeviceType {
	
	A1("人体红外与亮度感应器","0511"),
	A2("温湿度感应器(无线)","0521"),
	A3("温湿度感应器(有线)","4521"),
	A4("可燃气体感应器(无线)","0531"),
	A5("可燃气体感应器(有线)","4531"),
	
	B1("三键遥控器","8111"),
	B2("四键遥控器","8112"),
	
	C1("紧急按钮(正方形)","8121"),
	C2("紧急按钮(长方形)","8122"),
	
	D1("胁迫按钮","8131"),
	
	E1("吊坠式求助按钮","8141"),
	E2("腕式求助按钮","8142"),
	E3("拉绳式求助按钮","8143"),
	
	F1("无线门磁","8211"),
	
	G1("无线被动红外探测器","8221"),
	G2("双视窗被动红外探测器(电池款)","8222"),
	G3("双视窗被动红外探测器(DC电源款)","8223"),
	G4("吸顶式红外探测器(DC电源款)","8224"),
	
	H1("幕帘式被动红外探测器(电池款)","8231"),
	H2("幕帘式被动红外探测器(DC电源款)","8232"),
	
	I1("被动红外微波双鉴探测器","8241"),
	
	J1("报警控制器(数码管)","8311"),	
	J2("报警控制器(LCD屏、无短信模块)","8312"),
	J3("报警控制器(LCD屏、GSM模块)","8313"),
	J4("报警控制器(LCD屏、CDMA模块)","8314"),
	
	K1("报警控制面板","8321"),
	
	L1("双视窗被动红外探测器(有线)","C222"),
	L2("吸顶式红外探测器(有线)","C224"),
	
	M1("被动红外微波双鉴探测器(有线)","C241"),
	
	N1("报警控制面板(有线)","C321"),	
	
	UNDEFINE_DEVICE("末定义设备","0000");
	
	private String typeCode;
	private String typeName;
	
	private SenseDeviceType(String _typeName ,String _typeCode){
		this.typeCode = _typeCode;
		this.typeName = _typeName;
	}		
	
	public static String getDeviceTypeName(String typeCode){		
		SenseDeviceType[] allDeviceType = SenseDeviceType.values();
		
		for(SenseDeviceType dt : allDeviceType){		
			if(typeCode.equals(dt.getTypeCode())){
				return dt.getTypeName();
			}			
		}
				
		return SenseDeviceType.UNDEFINE_DEVICE.getTypeName();
	}
	

	//getter、setter
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}

