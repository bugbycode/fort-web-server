package com.bugbycode.module.server;

import java.io.Serializable;

public class ResourceServer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6116041728207444952L;
	
	private String userServerUrl;
	
	private String resourceServerUrl;

	public String getUserServerUrl() {
		return userServerUrl;
	}

	public void setUserServerUrl(String userServerUrl) {
		this.userServerUrl = userServerUrl;
	}

	public String getResourceServerUrl() {
		return resourceServerUrl;
	}

	public void setResourceServerUrl(String resourceServerUrl) {
		this.resourceServerUrl = resourceServerUrl;
	}
	
}
