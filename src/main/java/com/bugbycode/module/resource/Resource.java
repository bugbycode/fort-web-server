package com.bugbycode.module.resource;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.bugbycode.module.account.Account;

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
	
	private int useSsh;
	
	private int sshPort;
	
	private int useRdp;
	
	private int rdpPort;
	
	private int networkId;
	
	private String networkName;
	
	private Date createTime;
	
	private Date updateTime;
	
	private List<Account> accList;

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

	public int getUseSsh() {
		return useSsh;
	}

	public void setUseSsh(int useSsh) {
		this.useSsh = useSsh;
	}

	public int getSshPort() {
		return sshPort;
	}

	public void setSshPort(int sshPort) {
		this.sshPort = sshPort;
	}

	public int getUseRdp() {
		return useRdp;
	}

	public void setUseRdp(int useRdp) {
		this.useRdp = useRdp;
	}

	public int getRdpPort() {
		return rdpPort;
	}

	public void setRdpPort(int rdpPort) {
		this.rdpPort = rdpPort;
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
	
	public List<Account> getAccList() {
		return accList;
	}

	public void setAccList(List<Account> accList) {
		this.accList = accList;
	}

	public void copy(Resource r) {
		this.setId(r.getId());
		this.setName(r.getName());
		this.setIp(r.getIp());
		this.setStatus(r.getStatus());
		this.setType(r.getType());
		this.setOsType(r.getOsType());
		this.setUseSsh(r.getUseSsh());
		this.setSshPort(r.getSshPort());
		this.setUseRdp(r.getUseRdp());
		this.setRdpPort(r.getRdpPort());
		this.setNetworkId(r.getNetworkId());
		this.setNetworkName(r.getNetworkName());
		this.setCreateTime(r.getCreateTime());
		this.setUpdateTime(r.getUpdateTime());
		this.setAccList(r.getAccList());
	}
}
