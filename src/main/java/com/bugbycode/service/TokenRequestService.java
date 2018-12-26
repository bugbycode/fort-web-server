package com.bugbycode.service;

import com.alibaba.fastjson.JSONObject;
import com.bugbycode.module.login.LoginRole;
import com.bugbycode.module.token.UserAccessToken;
import com.util.http.HttpClient;

public class TokenRequestService {
	
	public UserAccessToken getToken(String url,String grant_type,String clientId,String clientSecret,
			String username,String password,String scope){
		HttpClient client = new HttpClient();
		String jsonStr = client.getToken(url, grant_type, username, password, clientId, clientSecret, scope);
		JSONObject json = JSONObject.parseObject(jsonStr);
		return JSONObject.toJavaObject(json, UserAccessToken.class);
	}
	
	public LoginRole checkToken(String url,String token,String clientId,String clientSecret) {
		HttpClient client = new HttpClient();
		String jsonStr = client.checkToken(url, token, clientId, clientSecret);
		JSONObject json = JSONObject.parseObject(jsonStr);
		return JSONObject.toJavaObject(json, LoginRole.class);
	}
}
