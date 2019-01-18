package com.bugbycode.module.resource_server;

import java.io.Serializable;

public class ResourceServer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2046291206534411338L;
	
	private int id;
	
	private int serverType;
	
	private int port;
	
	private int use;
	
	private String dbName;
	
	private int resId;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerType() {
		return serverType;
	}

	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getUse() {
		return use;
	}

	public void setUse(int use) {
		this.use = use;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}
}