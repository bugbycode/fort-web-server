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
	
	private int useSsh;
	
	private int useRdp;
	
	private int useSftp;
	
	private int useOracle;
	
	private int useSqlServer;
	
	private int useFtp;
	
	private int useTelnet;

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

	public int getUseSsh() {
		return useSsh;
	}

	public void setUseSsh(int useSsh) {
		this.useSsh = useSsh;
	}

	public int getUseRdp() {
		return useRdp;
	}

	public void setUseRdp(int useRdp) {
		this.useRdp = useRdp;
	}

	public int getUseSftp() {
		return useSftp;
	}

	public void setUseSftp(int useSftp) {
		this.useSftp = useSftp;
	}

	public int getUseOracle() {
		return useOracle;
	}

	public void setUseOracle(int useOracle) {
		this.useOracle = useOracle;
	}

	public int getUseSqlServer() {
		return useSqlServer;
	}

	public void setUseSqlServer(int useSqlServer) {
		this.useSqlServer = useSqlServer;
	}

	public int getUseFtp() {
		return useFtp;
	}

	public void setUseFtp(int useFtp) {
		this.useFtp = useFtp;
	}

	public int getUseTelnet() {
		return useTelnet;
	}

	public void setUseTelnet(int useTelnet) {
		this.useTelnet = useTelnet;
	}

	public void copy(Account acc) {
		this.setId(acc.getId());
		this.setAccount(acc.getAccount());
		this.setPassword(acc.getPassword());
		this.setType(acc.getType());
	}
}
