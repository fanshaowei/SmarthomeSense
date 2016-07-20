package cn.com.papi.smarthomesense.enums;

public enum SenseDeviceContorlUrl {
    SCENE_CONTROL("http://58.249.57.253:8011/smarthome/sceneControl?username=:username&reqToken=:reqToken&idScene=:idScene");
    
    private String url;
    private SenseDeviceContorlUrl(String url){
    	this.url = url;
    }
    
    
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
        
}
