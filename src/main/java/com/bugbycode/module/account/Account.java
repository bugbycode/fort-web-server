package com.bugbycode.module.account;

import java.io.Serializable;

public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String account;
	
	private String password;
	
	private int type;
	
	private int resourceId;
	
	private int serverType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public int getServerType() {
		return serverType;
	}

	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	public void copy(Account acc) {
		this.setId(acc.getId());
		this.setAccount(acc.getAccount());
		this.setPassword(acc.getPassword());
		this.setType(acc.getType());
		this.setResourceId(acc.getResourceId());
		this.setServerType(acc.getServerType());
	}
}
