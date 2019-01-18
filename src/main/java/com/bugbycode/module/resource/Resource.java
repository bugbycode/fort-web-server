package com.bugbycode.module.resource;

import java.io.Serializable;
import java.util.Date;


public class Resource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String name;
	
	private String ip;
	
	private int status;
	
	private String type;
	
	private String osType;
	
	private int networkId;
	
	private String networkName;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String serverList;
	
	private String accountList;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public int getNetworkId() {
		return networkId;
	}

	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getServerList() {
		return serverList;
	}

	public void setServerList(String serverList) {
		this.serverList = serverList;
	}

	public String getAccountList() {
		return accountList;
	}

	public void setAccountList(String accountList) {
		this.accountList = accountList;
	}

	public void copy(Resource r) {
		this.setId(r.getId());
		this.setName(r.getName());
		this.setIp(r.getIp());
		this.setStatus(r.getStatus());
		this.setType(r.getType());
		this.setOsType(r.getOsType());
		this.setNetworkId(r.getNetworkId());
		this.setNetworkName(r.getNetworkName());
		this.setCreateTime(r.getCreateTime());
		this.setUpdateTime(r.getUpdateTime());
	}
}
