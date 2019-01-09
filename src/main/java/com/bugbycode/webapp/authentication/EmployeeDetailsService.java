package com.bugbycode.webapp.authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bugbycode.module.login.LoginRole;
import com.bugbycode.module.login.LoginUserDetails;
import com.bugbycode.module.login.UserAuthentication;
import com.bugbycode.module.token.UserAccessToken;
import com.bugbycode.service.TokenRequestService;
import com.util.StringUtil;

public class EmployeeDetailsService implements UserDetailsService {

	private String clientId;
	
	private String clientSecret;
	
	private String refreshTokenUrl;
	
	private String checkTokenUrl;
	
	private TokenRequestService tokenRequestService;
	
	public EmployeeDetailsService(String clientId, String clientSecret, String refreshTokenUrl,
			String checkTokenUrl,TokenRequestService tokenRequestService) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.refreshTokenUrl = refreshTokenUrl;
		this.checkTokenUrl = checkTokenUrl;
		this.tokenRequestService = tokenRequestService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAuthentication auth = (UserAuthentication)SecurityContextHolder.getContext().getAuthentication();
		SecurityContextHolder.getContext().setAuthentication(null);
		UserAccessToken accessToken = auth.getToken();
		if(StringUtil.isNotBlank(accessToken.getError())) {
			throw new RuntimeException(accessToken.getError_description());
		}
		String token = auth.getToken().getAccess_token();
		LoginRole role = tokenRequestService.checkToken(checkTokenUrl, token, clientId, clientSecret);
		LoginUserDetails user = new LoginUserDetails();
		user.setPassword("");
		user.setUsername(username);
		user.setUserToken(accessToken);
		user.setRoleList(role.getAuthorities());
		return user;
	}

}
