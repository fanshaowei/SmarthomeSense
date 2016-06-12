package cn.com.papi.smarthomesense.service;

import javax.servlet.http.HttpServletRequest;

public interface IUserAuthenticationService {  
    public String userNameTokenAuthentication(HttpServletRequest request,String reqString);
}
