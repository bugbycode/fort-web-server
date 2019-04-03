package com.bugbycode.webapp.authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.alibaba.fastjson.JSONObject;
import com.bugbycode.config.AppConfig;
import com.bugbycode.module.log.LoginLog;
import com.bugbycode.module.login.UserAuthentication;
import com.bugbycode.module.token.UserAccessToken;
import com.bugbycode.service.DataRequestService;
import com.bugbycode.service.TokenRequestService;
import com.util.StringUtil;
import com.util.exception.ImgCodeAuthenticationException;
import com.util.exception.PasswordAuthenticationException;
import com.util.exception.UsernameAuthenticationException;

public class FortUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private String clientId;
	
	private String clientSecret;
	
	private String scope;
	
	private String tokenUrl;
	
	private String refreshTokenUrl;
	
	private String checkTokenUrl;
	
	private String logServerUrl;
	
	private TokenRequestService tokenRequestService;
	
	private DataRequestService dataRequestService;
	
	public FortUsernamePasswordAuthenticationFilter(String clientId, String clientSecret,String scope, String tokenUrl,
			String refreshTokenUrl, String checkTokenUrl,String logServerUrl,TokenRequestService tokenRequestService,
			DataRequestService dataRequestService) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.scope = scope;
		this.tokenUrl = tokenUrl;
		this.refreshTokenUrl = refreshTokenUrl;
		this.checkTokenUrl = checkTokenUrl;
		this.tokenRequestService = tokenRequestService;
		this.logServerUrl = logServerUrl;
		this.dataRequestService = dataRequestService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		HttpSession session = request.getSession();
		Map<String,Object> param = new HashMap<String,Object>();
		LoginLog log = new LoginLog();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String imgCode = request.getParameter("imgCode");
		Object randObj = session.getAttribute("rand");
		String rand = randObj == null ? "" : randObj.toString();
		session.setAttribute("loginName", username);
		session.setAttribute("password", password);
		log.setCreateTime(new Date().getTime());
		log.setSourceIp(request.getRemoteAddr());
		log.setUserLoginName(username);
		if(StringUtil.isBlank(username)) {
			param.put("jsonStr", JSONObject.toJSONString(log));
			dataRequestService.modifyLog(logServerUrl + AppConfig.LOGIN_LOG_SAVE_PATH, param);
			throw new UsernameAuthenticationException("请输入用户名");
		}else if(StringUtil.isEmpty(password)) {
			param.put("jsonStr", JSONObject.toJSONString(log));
			dataRequestService.modifyLog(logServerUrl + AppConfig.LOGIN_LOG_SAVE_PATH, param);
			throw new PasswordAuthenticationException("请输入密码");
		}else if(StringUtil.isBlank(imgCode)) {
			param.put("jsonStr", JSONObject.toJSONString(log));
			dataRequestService.modifyLog(logServerUrl + AppConfig.LOGIN_LOG_SAVE_PATH, param);
			throw new ImgCodeAuthenticationException("请输入验证码");
		}else if(!imgCode.toUpperCase().equals(rand)) {
			param.put("jsonStr", JSONObject.toJSONString(log));
			dataRequestService.modifyLog(logServerUrl + AppConfig.LOGIN_LOG_SAVE_PATH, param);
			throw new ImgCodeAuthenticationException("验证码错误");
		}
		session.removeAttribute("loginName");
		session.removeAttribute("password");
		UserAccessToken token = null;
		try {
			token = this.tokenRequestService.getToken(tokenUrl, "password", clientId, clientSecret, username, password, scope);
		}catch (Exception e) {
			param.put("jsonStr", JSONObject.toJSONString(log));
			dataRequestService.modifyLog(logServerUrl + AppConfig.LOGIN_LOG_SAVE_PATH, param);
			throw new UsernameAuthenticationException("身份认证服务器故障");
		}
		
		try {
			log.setStatus(1);
			SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(token));
			return super.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, ""));
		}catch (Exception e) {
			log.setStatus(0);
			throw e;
		}finally {
			param.put("jsonStr", JSONObject.toJSONString(log));
			dataRequestService.modifyLog(logServerUrl + AppConfig.LOGIN_LOG_SAVE_PATH, param);
		}
	}
	
}
