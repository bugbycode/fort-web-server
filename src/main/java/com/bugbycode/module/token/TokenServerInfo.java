package com.bugbycode.module.token;

import java.io.Serializable;

public class TokenServerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 626704807652636119L;

	private String clientId;
	
	private String clientSecret;
	
	private String scope;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
