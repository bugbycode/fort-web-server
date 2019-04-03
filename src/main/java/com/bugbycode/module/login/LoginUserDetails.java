package com.bugbycode.module.login;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bugbycode.config.RoleConfig;
import com.bugbycode.module.token.UserAccessToken;

public class LoginUserDetails implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3331276159128426543L;

	private String name;
	
	private String username;
	
	private String password;
	
	private UserAccessToken userToken;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	public void setRoleList(Set<String> roleList) {
		Set<GrantedAuthority> grant = new HashSet<GrantedAuthority>();
		if(!CollectionUtils.isEmpty(roleList)) {
			roleList.add("ROLE_" + RoleConfig.LOGIN_USER);
			for(String role : roleList) {
				grant.add(new SimpleGrantedAuthority(role));
			}
		}
		this.authorities = grant;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public UserAccessToken getUserToken() {
		return userToken;
	}

	public void setUserToken(UserAccessToken userToken) {
		this.userToken = userToken;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
