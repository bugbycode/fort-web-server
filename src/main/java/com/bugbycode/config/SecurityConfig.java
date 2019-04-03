package com.bugbycode.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bugbycode.module.server.ResourceServer;
import com.bugbycode.module.token.TokenServerInfo;
import com.bugbycode.service.DataRequestService;
import com.bugbycode.service.TokenRequestService;
import com.bugbycode.webapp.authentication.EmployeeDetailsService;
import com.bugbycode.webapp.authentication.FortAuthenticationFailureHandler;
import com.bugbycode.webapp.authentication.FortAuthenticationSuccessHandler;
import com.bugbycode.webapp.authentication.FortUsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final String defaultFailureUrl = "/login?error";
	
	@Value("${spring.web.oauth.clientId}")
	private String clientId;
	
	@Value("${spring.web.oauth.clientSecret}")
	private String clientSecret;
	
	@Value("${spring.web.oauth.scope}")
	private String scope;
	
	@Value("${spring.web.oauth.tokenUrl}")
	private String tokenUrl;
	
	@Value("${spring.web.oauth.refreshTokenUrl}")
	private String refreshTokenUrl;
	
	@Value("${spring.web.oauth.checkTokenUrl}")
	private String checkTokenUrl;
	
	@Value("${spring.resource.server.logServerUrl}")
	private String logServerUrl;
	
	@Value("${spring.resource.server.userServerUrl}")
	private String userServerUrl;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private DataRequestService dataRequestService;
	
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		
		.antMatchers("/imgCode").permitAll()
		//凡是登录成功的用户都可访问
		.antMatchers("/main").hasRole(RoleConfig.LOGIN_USER)
		
		.antMatchers("/user/query","/user/edit",
				"/user/checkUserName","/user/queryGroup",
				"/user/queryRole")
		.hasAnyRole(RoleConfig.USER_QUERY,RoleConfig.USER_DELETE,
				RoleConfig.USER_INSERT,RoleConfig.USER_UPDATE)
		
		.antMatchers("/user/update").hasRole(RoleConfig.USER_UPDATE)
		.antMatchers("/user/insert").hasRole(RoleConfig.USER_INSERT)
		.antMatchers("/user/delete").hasRole(RoleConfig.USER_DELETE)
		
		//用户分组
		.antMatchers("/userGroup/query","/userGroup/edit","/userGroup/queryRole","/userGroup/checkGroupName",
				"/userGroup/queryUser")
			.hasAnyRole(RoleConfig.USER_GROUP_QUERY,RoleConfig.USER_GROUP_DELETE,
				RoleConfig.USER_GROUP_INSERT,RoleConfig.USER_GROUP_UPDATE)
			
		.antMatchers("/userGroup/update").hasRole(RoleConfig.USER_GROUP_UPDATE)
		.antMatchers("/userGroup/insert").hasRole(RoleConfig.USER_GROUP_INSERT)
		.antMatchers("/userGroup/delete").hasRole(RoleConfig.USER_GROUP_DELETE)
		
		//角色
		.antMatchers("/role/query","/role/edit",
				"/role/checkRoleName")
		.hasAnyRole(RoleConfig.ROLE_QUERY,RoleConfig.ROLE_DELETE,
				RoleConfig.ROLE_INSERT,RoleConfig.ROLE_UPDATE)
		
		.antMatchers("/role/update").hasRole(RoleConfig.ROLE_UPDATE)
		.antMatchers("/role/insert").hasRole(RoleConfig.ROLE_INSERT)
		.antMatchers("/role/delete").hasRole(RoleConfig.ROLE_DELETE)
			
		//资产管理
		.antMatchers("/resource/query","/resource/edit",
				"/resource/checkResName","/resource/queryNetWork")
		.hasAnyRole(RoleConfig.RESOURCE_QUERY,RoleConfig.RESOURCE_DELETE,
				RoleConfig.RESOURCE_INSERT,RoleConfig.RESOURCE_UPDATE)
		
		.antMatchers("/resource/update").hasRole(RoleConfig.RESOURCE_UPDATE)
		.antMatchers("/resource/insert").hasRole(RoleConfig.RESOURCE_INSERT)
		.antMatchers("/resource/delete").hasRole(RoleConfig.RESOURCE_DELETE)
		
		//网络管理
		.antMatchers("/network/query","/network/edit",
				"/network/checkName")
		.hasAnyRole(RoleConfig.NETWORK_QUERY,RoleConfig.NETWORK_DELETE,
				RoleConfig.NETWORK_INSERT,RoleConfig.NETWORK_UPDATE)
		
		.antMatchers("/network/update").hasRole(RoleConfig.NETWORK_UPDATE)
		.antMatchers("/network/insert").hasRole(RoleConfig.NETWORK_INSERT)
		.antMatchers("/network/delete").hasRole(RoleConfig.NETWORK_DELETE)
		
		//授权规则
		.antMatchers("/rule/update").hasRole(RoleConfig.RULE_UPDATE)
		.antMatchers("/rule/insert").hasRole(RoleConfig.RULE_INSERT)
		.antMatchers("/rule/delete").hasRole(RoleConfig.RULE_DELETE)
		
		.antMatchers("/rule/query","/rule/queryById","/rule/queryResource",
				"/rule/queryUser","/rule/queryAccount","/rule/edit")
			.hasAnyRole(RoleConfig.RULE_QUERY,RoleConfig.RULE_DELETE,
				RoleConfig.RULE_INSERT,RoleConfig.RULE_UPDATE)
			
		//访问记录
		.antMatchers("/loginLog/query").hasRole(RoleConfig.LOGIN_LOG_QUERY)	
		
		//操作记录
		.antMatchers("/systemLog/query").hasRole(RoleConfig.SYSTEM_LOG_QUERY)	
		
		.and().headers().frameOptions().disable()
		//用户登录页面 所有人均可访问
				.and().formLogin().loginPage("/login").permitAll()
				.and().logout().invalidateHttpSession(true)
				.and().addFilterBefore(getUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	private Filter getUsernamePasswordAuthenticationFilter() throws Exception {
		UsernamePasswordAuthenticationFilter authFilter = 
				new FortUsernamePasswordAuthenticationFilter(clientId, clientSecret, 
						scope,tokenUrl, refreshTokenUrl, checkTokenUrl,logServerUrl,tokenRequestService(),dataRequestService);
		authFilter.setAuthenticationManager(this.authenticationManager());
		authFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		authFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		return authFilter;
	}
	
	@Bean("authenticationFailureHandler")
	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return new FortAuthenticationFailureHandler(defaultFailureUrl);
	}
	
	@Bean("authenticationSuccessHandler")
	public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		return new FortAuthenticationSuccessHandler();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	/*
	@Bean("userDetailsService")
	public UserDetailsService getUserDetailsService() {
		return new EmployeeDetailsService(clientId, clientSecret, 
				refreshTokenUrl, checkTokenUrl,userServerUrl,tokenRequestService(),dataRequestService);
	}*/
	
	@Bean
	public PasswordEncoder passwordEncoder() {

		return new PasswordEncoder() {

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return rawPassword.toString().equals(encodedPassword);
			}

			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}
		};
	}
	
	@Bean
	public TokenRequestService tokenRequestService() {
		return new TokenRequestService();
	}
	
	@Bean
	@ConfigurationProperties(prefix="spring.resource.server")
	public ResourceServer resourceServer() {
		return new ResourceServer();
	}
	
	@Bean
	@ConfigurationProperties(prefix="spring.web.oauth")
	public TokenServerInfo tokenServerInfo() {
		return new TokenServerInfo();
	}
}
