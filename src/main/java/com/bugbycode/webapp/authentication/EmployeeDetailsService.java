package com.bugbycode.webapp.authentication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bugbycode.config.AppConfig;
import com.bugbycode.module.login.LoginRole;
import com.bugbycode.module.login.LoginUserDetails;
import com.bugbycode.module.login.UserAuthentication;
import com.bugbycode.module.token.UserAccessToken;
import com.bugbycode.module.user.User;
import com.bugbycode.service.DataRequestService;
import com.bugbycode.service.TokenRequestService;
import com.util.StringUtil;

@Service("userDetailsService")
public class EmployeeDetailsService implements UserDetailsService {

	@Value("${spring.web.oauth.clientId}")
	private String clientId;
	
	@Value("${spring.web.oauth.clientSecret}")
	private String clientSecret;
	
	@Value("${spring.web.oauth.checkTokenUrl}")
	private String checkTokenUrl;
	
	@Value("${spring.resource.server.userServerUrl}")
	private String userServerUrl;
	
	@Autowired
	private TokenRequestService tokenRequestService;
	
	@Autowired
	private DataRequestService dataRequestService;
	
	/*
	public EmployeeDetailsService(String clientId, String clientSecret, String refreshTokenUrl,
			String checkTokenUrl,String userServerUrl,TokenRequestService tokenRequestService,
			DataRequestService dataRequestService) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.refreshTokenUrl = refreshTokenUrl;
		this.checkTokenUrl = checkTokenUrl;
		this.userServerUrl = userServerUrl;
		this.tokenRequestService = tokenRequestService;
		this.dataRequestService = dataRequestService;
	}*/

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAuthentication auth = (UserAuthentication)SecurityContextHolder.getContext().getAuthentication();
		SecurityContextHolder.getContext().setAuthentication(null);
		UserAccessToken accessToken = auth.getToken();
		if(StringUtil.isNotBlank(accessToken.getError())) {
			throw new RuntimeException(accessToken.getError_description());
		}
		String token = auth.getToken().getAccess_token();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("userName", username);
		User u = dataRequestService.query(userServerUrl + AppConfig.USER_QUERY_BY_USERNAME_PATH, param,token, User.class);
		LoginRole role = tokenRequestService.checkToken(checkTokenUrl, token, clientId, clientSecret);
		LoginUserDetails user = new LoginUserDetails();
		user.setName(u.getName());
		user.setPassword("");
		user.setUsername(username);
		user.setUserToken(accessToken);
		user.setRoleList(role.getAuthorities());
		return user;
	}

}
