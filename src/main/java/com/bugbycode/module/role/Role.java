package com.bugbycode.module.role;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.bugbycode.config.RoleConfig;
import com.util.StringUtil;

public class Role implements Serializable{

	public static final String LOGIN = "LOGIN"; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4073044370792272927L;

	private int id;
	
	private String name;
	
	private String description;
	
	private String grantedAuthority;

	private int type;
	
	private Date createTime;
	
	private Date updateTime;
	
	private int userQuery = 0;
	
	private int userInsert = 0;
	
	private int userUpdate = 0;
	
	private int userDelete = 0;
	
	private int userGroupQuery = 0;
	
	private int userGroupInsert = 0;
	
	private int userGroupUpdate = 0;
	
	private int userGroupDelete = 0;
	
	private int roleQuery = 0;
	
	private int roleInsert = 0;
	
	private int roleUpdate = 0;
	
	private int roleDelete = 0;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGrantedAuthority() {
		return grantedAuthority;
	}

	public void setGrantedAuthority(String grantedAuthority) {
		this.grantedAuthority = grantedAuthority;
		Set<String> set = new HashSet<String>();
		if(StringUtil.isNotBlank(this.grantedAuthority)) {
			set = StringUtils.commaDelimitedListToSet(this.grantedAuthority);
			for(String roleStr : set) {
				switch (roleStr) {
					case RoleConfig.USER_QUERY:
						this.userQuery = 1;
						break;
					case RoleConfig.USER_INSERT:
						this.userInsert = 1;
						break;
					case RoleConfig.USER_UPDATE:
						this.userUpdate = 1;
						break;
					case RoleConfig.USER_DELETE:
						this.userDelete = 1;
						break;
					case RoleConfig.USER_GROUP_QUERY:
						this.userGroupQuery = 1;
						break;
					case RoleConfig.USER_GROUP_INSERT:
						this.userGroupInsert = 1;
						break;
					case RoleConfig.USER_GROUP_UPDATE:
						this.userGroupUpdate = 1;
						break;
					case RoleConfig.USER_GROUP_DELETE:
						this.userGroupDelete = 1;
						break;
					case RoleConfig.ROLE_QUERY:
						this.roleQuery = 1;
						break;
					case RoleConfig.ROLE_INSERT:
						this.roleInsert = 1;
						break;
					case RoleConfig.ROLE_UPDATE:
						this.roleUpdate = 1;
						break;
					case RoleConfig.ROLE_DELETE:
						this.roleDelete = 1;
						break;
					default:
						break;
				}
			}
		}
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
	
	public int getUserQuery() {
		return userQuery;
	}

	public int getUserInsert() {
		return userInsert;
	}

	public int getUserUpdate() {
		return userUpdate;
	}

	public int getUserDelete() {
		return userDelete;
	}

	public int getUserGroupQuery() {
		return userGroupQuery;
	}

	public int getUserGroupInsert() {
		return userGroupInsert;
	}

	public int getUserGroupUpdate() {
		return userGroupUpdate;
	}

	public int getUserGroupDelete() {
		return userGroupDelete;
	}

	public int getRoleQuery() {
		return roleQuery;
	}

	public int getRoleInsert() {
		return roleInsert;
	}

	public int getRoleUpdate() {
		return roleUpdate;
	}

	public int getRoleDelete() {
		return roleDelete;
	}

	public void copy(Role r) {
		this.setId(r.getId());
		this.setName(r.getName());
		this.setDescription(r.getDescription());
		this.setType(r.getType());
		this.setGrantedAuthority(r.getGrantedAuthority());
		this.setCreateTime(r.getCreateTime());
		this.setUpdateTime(r.getUpdateTime());
	}
}