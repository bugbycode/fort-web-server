package com.bugbycode.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bugbycode.module.login.LoginUserDetails;
import com.bugbycode.module.token.UserAccessToken;
import com.util.http.HttpClient;
import com.util.page.Page;
import com.util.page.SearchResult;

@Service("dataRequestService")
public class DataRequestService {
	
	public <T> SearchResult<T> search(String path,Map<String,Object> param,Class<T> claszz) {
		LoginUserDetails loginUser = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserAccessToken accessToken = loginUser.getUserToken();
		HttpClient client = new HttpClient();
		String jsonStr = client.getResource(path, accessToken.getAccess_token(), param);
		JSONObject json = JSONObject.parseObject(jsonStr);
		if(json.containsKey("error")) {
			String description = json.getString("error_description");
			throw new RuntimeException(json.getString(description));
		}
		SearchResult<T> sr = new SearchResult<T>();
		List<T> list = new ArrayList<T>();
		JSONArray arr = json.getJSONArray("data");
		JSONObject pageJson = json.getJSONObject("page");
		if(!(arr == null || arr.isEmpty())) {
			list = JSONObject.parseArray(arr.toString(), claszz);
		}
		sr.setList(list);
		if(!(pageJson == null)) {
			Page page = pageJson.toJavaObject(Page.class);
			sr.setPage(page);
		}
		return sr;
	}
	
	public <T> T query(String path,Map<String,Object> param,Class<T> claszz) {
		LoginUserDetails loginUser = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserAccessToken accessToken = loginUser.getUserToken();
		HttpClient client = new HttpClient();
		String jsonStr = client.getResource(path, accessToken.getAccess_token(), param);
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
		String jsonStr = client.getResource(path, accessToken.getAccess_token(), param);
		JSONObject json = JSONObject.parseObject(jsonStr);
		if(json.containsKey("error")) {
			String description = json.getString("error_description");
			throw new RuntimeException(json.getString(description));
		}
		return json;
	}
}
