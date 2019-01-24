package com.bugbycode.config;

public class AppConfig {
	//用户管理
	public final static String USER_QUERY_PATH = "/user/query";
	
	public final static String USER_QUERY_ROLE_PATH = "/user/queryRole";
	
	public final static String USER_QUERY_GROUP_PATH = "/user/queryGroup";
	
	public final static String USER_QUERY_BYID_PATH = "/user/queryByUserId";
	
	public final static String USER_QUERY_BY_USERNAME_PATH = "/user/queryByUserName";
	
	public final static String USER_INSERT_PATH = "/user/insert";
	
	public final static String USER_UPDATE_PATH = "/user/update";
	
	public final static String USER_DELETE_PATH = "/user/delete";
	
	//分组管理
	public final static String USER_GROUP_QUERY_PATH = "/userGroup/query";
	
	public final static String USER_GROUP_QUERY_BYID_PATH = "/userGroup/queryByGroupId";
	
	public final static String USER_GROUP_QUERY_BY_GROUPNAME_PATH = "/userGroup/queryByGroupName";
	
	public final static String USER_GROUP_QUERY_ROLE_PATH = "/userGroup/queryRole";
	
	public final static String USER_GROUP_INSERT_PATH = "/userGroup/insert";
	
	public final static String USER_GROUP_UPDATE_PATH = "/userGroup/update";
	
	public final static String USER_GROUP_DELETE_PATH = "/userGroup/delete";
	
	//角色管理
	public final static String ROLE_QUERY_PATH = "/role/query";
	
	public final static String ROLE_INSERT_PATH = "/role/insert";
	
	public final static String ROLE_UPDATE_PATH = "/role/update";
	
	public final static String ROLE_DELETE_PATH = "/role/delete";
	
	public final static String ROLE_QUERY_BYID_PATH = "/role/queryByRoleId";
	
	public final static String ROLE_QUERY_BY_ROLENAME = "/role/queryByRoleName";
	
	//资产管理
	public final static String RESOURCE_QUERY_PATH = "/resource/query";
	
	public final static String RESOURCE_QUERY_BYID_PATH = "/resource/queryById";
	
	public final static String RESOURCE_QUERY_BY_NAME_PATH = "/resource/queryByName";
	
	public final static String RESOURCE_QUERY_BY_IP_PATH = "/resource/queryByIp";
	
	public final static String RESOURCE_INSERT_PATH = "/resource/insert";
	
	public final static String RESOURCE_UPDATE_PATH = "/resource/update";
	
	public final static String RESOURCE_DELETE_PATH = "/resource/delete";
	
	public final static String RESOURCE_SERVER_QUERY_PATH = "/resourceServer/query";
	
	public final static String RESOURCE_SERVER_INSERT_PATH = "/resourceServer/insert";
	
	public final static String ACCOUNT_QUERY_PATH = "/account/query";
	
	public final static String ACCOUNT_INSERT_PATH = "/account/insert";
	
	public final static String ACCOUNT_UPDATE_PATH = "/account/update";
	
	public final static String ACCOUNT_DELETE_PATH = "/account/delete";
	
	public final static String ACCOUNT_INSERT_REL_PATH = "/account/insertRel";
	
	public final static String RESOURCE_NETWORK_QUERY_PATH = "/resource/queryNetWork";
	
	//网络信息
	public final static String NETWORK_QUERY_PATH = "/network/query";
	
	public final static String NETWORK_INSERT_PATH = "/network/insert";
	
	public final static String NETWORK_UPDATE_PATH = "/network/update";
	
	public final static String NETWORK_DELETE_PATH = "/network/delete";
	
	public final static String NETWORK_QUERY_BYID_PATH = "/network/queryById";
	
	public final static String NETWORK_QUERY_BY_NAME_PATH = "/network/queryByName";
	
	//协议服务类型
	public final static int USE_SSH = 0;
	
	public final static int USE_SFTP = 1;
	
	public final static int USE_RDP = 2;
	
	public final static int USE_TELNET = 3;
	
	public final static int USE_ORACLE = 4;
	
	public final static int USE_SQLSERVER = 5;
	
	public final static int USE_FTP = 6;
	
}
