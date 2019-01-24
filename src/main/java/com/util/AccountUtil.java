package com.util;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.bugbycode.config.AppConfig;
import com.bugbycode.module.account.Account;

public class AccountUtil {
	
	public static Account findAccount(String account,int type,List<Account> accList) {
		if(!CollectionUtils.isEmpty(accList)) {
			for(Account acc : accList) {
				if(!account.equals(acc.getAccount())) {
					continue;
				}
				switch (type) {
					case AppConfig.USE_SSH:
						if(acc.getUseSsh() == 1) {
							return acc;
						}
						break;
					case AppConfig.USE_SFTP:
						if(acc.getUseSftp() == 1) {
							return acc;
						}
						break;
					case AppConfig.USE_RDP:
						if(acc.getUseRdp() == 1) {
							return acc;
						}
						break;
					case AppConfig.USE_FTP:
						if(acc.getUseFtp() == 1) {
							return acc;
						}
						break;
					case AppConfig.USE_TELNET:
						if(acc.getUseTelnet() == 1) {
							return acc;
						}
						break;
					case AppConfig.USE_ORACLE:
						if(acc.getUseOracle() == 1) {
							return acc;
						}
						break;
					case AppConfig.USE_SQLSERVER:
						if(acc.getUseSqlServer() == 1) {
							return acc;
						}
						break;
					default:
						break;
				}
			}
		}
		return null;
	}
}
