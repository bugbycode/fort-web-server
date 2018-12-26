package com.test;

import org.junit.Test;

import com.util.http.HttpClient;

public class TestToken {

	private HttpClient client = new HttpClient();
	
	@Test
	public void testToken() {
		String tokenStr = client.getToken("http://dev.server:9999/oauth/token", "password", "admin", "admin1234", "fort", "j1d1sec.c0m", "web");
		System.out.println(tokenStr);
	}
	
	@Test
	public void checkToken() {
		String role = client.checkToken("http://dev.server:9999/oauth/check_token", "bf1b0e14-7a31-4af4-acf5-a68855544274", "fort", "j1d1sec.c0m");
		System.out.println(role);
	}
	
	@Test
	public void refreshToken() {
		String result = client.refreshToken("http://dev.server:9999/oauth/token", "refresh_token", "fort", "j1d1sec.c0m", "804dc6e0-b020-4a99-ab22-be340b8b0fbb");
		System.out.println(result);
	}
}
