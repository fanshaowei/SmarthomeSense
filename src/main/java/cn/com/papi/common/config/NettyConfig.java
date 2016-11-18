package cn.com.papi.common.config;

public class NettyConfig {
	private String hostServer;
	private int portServer;
	
	public NettyConfig(){
		System.out.println("--------------获取nettyServer配置--------------------------");
	}
		
	public NettyConfig(String hostServer, int portServer) {
		super();
		this.hostServer = hostServer;
		this.portServer = portServer;
	}

	public String getHostServer() {
		return hostServer;
	}

	public void setHostServer(String hostServer) {
		this.hostServer = hostServer;
	}

	public int getPortServer() {
		return portServer;
	}

	public void setPortServer(int portServer) {
		this.portServer = portServer;
	}
	
}
