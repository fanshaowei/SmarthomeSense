package cn.com.papi.smarthomesense.bean;

import java.io.Serializable;

public class UserBean implements Serializable{

	private static final long serialVersionUID = 1L;

	
	private int uid;
	private String username;
	private String password;
	private String token;
	private Boolean isLogin;
	private String nickName;
	private int dyId;

	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getDyId() {
		return dyId;
	}
	public void setDyId(int dyId) {
		this.dyId = dyId;
	}
	
	@Override
	public String toString() {
		return "uid:" + uid + "\tusername:" + username +
				"\tpassword:" + password + "\tisLogin:" + isLogin;
	}
	public Boolean getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}

}
