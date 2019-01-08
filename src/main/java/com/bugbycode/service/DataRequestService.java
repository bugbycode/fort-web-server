package com.bugbycode.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bugbycode.module.login.LoginUserDetails;
import com.bugbycode.module.server.ResourceServer;
import com.bugbycode.module.token.UserAccessToken;
import com.bugbycode.module.user.User;
import com.util.http.HttpClient;

@Service("dataRequestService")
public class DataRequestService {
	
	@Autowired
	private ResourceServer resourceServer;
	
	public <T> T search(String path,Map<String,Object> param,Class<T> claszz,Class<User> module) {
		LoginUserDetails loginUser = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserAccessToken accessToken = loginUser.getUserToken();
		HttpClient client = new HttpClient();
		String url = resourceServer.getUserServerUrl() + path;
		String jsonStr = client.getResource(url, accessToken.getAccess_token(), param);
		JSONObject json = JSONObject.parseObject(jsonStr);
		if(json.containsKey("error")) {
			String description = json.getString("error_description");
			throw new RuntimeException(json.getString(description));
		}
		List<Object> list = new ArrayList<Object>();
		JSONObject result = new JSONObject();
		JSONArray arr = json.getJSONArray("data");
		if(!(arr == null || arr.isEmpty())) {
			for(int index = 0;index < arr.size();index++) {
				list.add(arr.getJSONObject(index).toJavaObject(module));
			}
		}
		result.put("list", list);
		result.put("page", json.getJSONObject("page"));
		return result.toJavaObject(claszz);
	}
	
	public <T> T query(String path,Map<String,Object> param,Class<T> claszz) {
		LoginUserDetails loginUser = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserAccessToken accessToken = loginUser.getUserToken();
		HttpClient client = new HttpClient();
		String url = resourceServer.getUserServerUrl() + path;
		String jsonStr = client.getResource(url, accessToken.getAccess_token(), param);
		JSONObject json = JSONObject.parseObject(jsonStr);
		if(json.containsKey("error")) {
			String description = json.getString("error_description");
			throw new RuntimeException(json.getString(description));
		}
		if(json.containsKey("data")) {
			JSONObject jsonObj = json.getJSONObject("data");
			return jsonObj.toJavaObject(claszz);
		}
		return null;
	}
	
	public JSONObject modify(String path,Map<String,Object> param) {
		LoginUserDetails loginUser = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserAccessToken accessToken = loginUser.getUserToken();
		HttpClient client = new HttpClient();
		String url = resourceServer.getUserServerUrl() + path;
		String jsonStr = client.getResource(url, accessToken.getAccess_token(), param);
		JSONObject json = JSONObject.parseObject(jsonStr);
		if(json.containsKey("error")) {
			String description = json.getString("error_description");
			throw new RuntimeException(json.getString(description));
		}
		return json;
	}
}
