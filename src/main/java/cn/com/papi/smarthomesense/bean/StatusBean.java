package cn.com.papi.smarthomesense.bean;

public class StatusBean<T> {
	private boolean status;
	private String code;
	private String msg;
	private T data;
	
	public StatusBean() {
		
	}
	
	public StatusBean(boolean status, String msg) {
		this.status = status;
		this.msg = msg;
	}
	
	public StatusBean(boolean status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
    public StatusBean(boolean status, String code, String msg, T data)  {
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public StatusBean(boolean status, T data) {
        this.status = status;
        msg = "";
        this.data = data;
    }

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
    
    
}
