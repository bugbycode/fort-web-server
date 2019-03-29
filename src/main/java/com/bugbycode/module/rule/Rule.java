package com.bugbycode.module.rule;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Rule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8370027810839554220L;
	
	private int id;
	
	private int empId;
	
	private String empName;
	
	private String empUserName;
	
	private int empGroupId;
	
	private String empGroupName;
	
	private int empRoleId;
	
	private String empRoleName;
	
	private int resId;
	
	private String resName;
	
	private String resIp;
	
	private int networkId;
	
	private String networkName;
	
	private String resType;
	
	private String resOsType;
	
	private Date createTime;
	
	private Date updateTime;
	
	private List<RuleAccount> accountList;
	
	private String userInfo;
	
	private String resInfo;
	
	private String accInfo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpUserName() {
		return empUserName;
	}

	public void setEmpUserName(String empUserName) {
		this.empUserName = empUserName;
	}

	public int getEmpGroupId() {
		return empGroupId;
	}

	public void setEmpGroupId(int empGroupId) {
		this.empGroupId = empGroupId;
	}

	public String getEmpGroupName() {
		return empGroupName;
	}

	public void setEmpGroupName(String empGroupName) {
		this.empGroupName = empGroupName;
	}

	public int getEmpRoleId() {
		return empRoleId;
	}

	public void setEmpRoleId(int empRoleId) {
		this.empRoleId = empRoleId;
	}

	public String getEmpRoleName() {
		return empRoleName;
	}

	public void setEmpRoleName(String empRoleName) {
		this.empRoleName = empRoleName;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResIp() {
		return resIp;
	}

	public void setResIp(String resIp) {
		this.resIp = resIp;
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

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getResOsType() {
		return resOsType;
	}

	public void setResOsType(String resOsType) {
		this.resOsType = resOsType;
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

	public List<RuleAccount> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<RuleAccount> accountList) {
		this.accountList = accountList;
	}
	
	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getResInfo() {
		return resInfo;
	}

	public void setResInfo(String resInfo) {
		this.resInfo = resInfo;
	}

	public String getAccInfo() {
		return accInfo;
	}

	public void setAccInfo(String accInfo) {
		this.accInfo = accInfo;
	}

	public void copy(Rule r) {
		this.setId(r.getId());
		this.setEmpId(r.getEmpId());
		this.setEmpName(r.getEmpName());
		this.setEmpUserName(r.getEmpUserName());
		this.setEmpGroupId(r.getEmpGroupId());
		this.setEmpGroupName(r.getEmpGroupName());
		this.setEmpRoleId(r.getEmpRoleId());
		this.setEmpRoleName(r.getEmpRoleName());
		this.setResId(r.getResId());
		this.setResName(r.getResName());
		this.setResIp(r.getResIp());
		this.setNetworkId(r.getNetworkId());
		this.setNetworkName(r.getNetworkName());
		this.setResType(r.getResType());
		this.setResOsType(r.getResOsType());
		this.setCreateTime(r.getCreateTime());
		this.setUpdateTime(r.getUpdateTime());
		this.setAccountList(r.getAccountList());
	}
}
