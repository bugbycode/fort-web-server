package com.bugbycode.module.login;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.bugbycode.module.token.UserAccessToken;

public class UserAuthentication extends AbstractAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8173599710530504760L;

	private UserAccessToken token;
	
	public UserAuthentication(UserAccessToken token) {
		super(null);
		this.token = token;
	}

	@Override
	public Object getCredentials() {
		throw new RuntimeException("函数未实现");
	}

	@Override
	public Object getPrincipal() {
		throw new RuntimeException("函数未实现");
	}

	public UserAccessToken getToken() {
		return token;
	}

}
