package com.bugbycode.module.device;

import java.io.Serializable;
import java.util.Set;

public class DeviceObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5604902791302167766L;

	private int ruleId;
	
	private int deviceId;
	
	private String deviceName;
	
	private String deviceIp;
	
	private int networkId;
	
	private String networkName;
	
	private String deviceType;
	
	private String deviceOs;
	
	private Set<String> accountList;

	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
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

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceOs() {
		return deviceOs;
	}

	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}

	public Set<String> getAccountList() {
		return accountList;
	}

	public void setAccountList(Set<String> accountList) {
		this.accountList = accountList;
	}
}
