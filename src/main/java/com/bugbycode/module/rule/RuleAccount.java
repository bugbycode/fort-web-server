package com.bugbycode.module.rule;

import java.io.Serializable;

public class RuleAccount implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6950017168542873683L;

	private int id;
	
	private String account;
	
	private int ruleId;

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

	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}	
}
