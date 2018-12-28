package com.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bugbycode.config.AppConfig;
import com.bugbycode.module.user.User;
import com.bugbycode.service.DataRequestService;
import com.util.http.HttpClient;
import com.util.page.SearchResult;

public class TestToken {

	private HttpClient client = new HttpClient();
	
	@Test
	public void testToken() {
		String tokenStr = client.getToken("http://dev.server:9999/oauth/token", "password", "admin", "admin1234", "fort", "j1d1sec.c0m", "web");
		System.out.println(tokenStr);
	}
	
	@Test
	public void checkToken() {
		String role = client.checkToken("http://dev.server:9999/oauth/check_token", "a2120315-fb9c-4497-b071-31b30cf6ac95", "fort", "j1d1sec.c0m");
		System.out.println(role);
	}
	
	@Test
	public void refreshToken() {
		String result = client.refreshToken("http://dev.server:9999/oauth/token", "refresh_token", "fort", "j1d1sec.c0m", "804dc6e0-b020-4a99-ab22-be340b8b0fbb");
		System.out.println(result);
		
		DataRequestService service = new DataRequestService();
		Map<String,Object> param = new HashMap<String,Object>();
		//SearchResult<User> sr = service.search(AppConfig.USER_QUERY_PATH,param, null);
	}
}
