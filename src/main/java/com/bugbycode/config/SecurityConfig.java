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
	
	@Autowired
	private UserDetailsService userDetailsService;
	
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
		
		.antMatchers("/user/query").hasAnyRole(RoleConfig.USER_QUERY,RoleConfig.USER_DELETE)
		.antMatchers("/user/edit","/user/checkUserName","/user/queryGroup").hasAnyRole(
				RoleConfig.USER_QUERY,RoleConfig.USER_INSERT,
				RoleConfig.USER_UPDATE)
		.antMatchers("/user/update").hasRole(RoleConfig.USER_UPDATE)
		.antMatchers("/user/insert").hasRole(RoleConfig.USER_INSERT)
		.antMatchers("/user/delete").hasRole(RoleConfig.USER_DELETE)
		
		.and().headers().frameOptions().disable()
		//用户登录页面 所有人均可访问
				.and().formLogin().loginPage("/login").permitAll()
				.and().logout().invalidateHttpSession(true)
				.and().addFilterBefore(getUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	private Filter getUsernamePasswordAuthenticationFilter() throws Exception {
		UsernamePasswordAuthenticationFilter authFilter = 
				new FortUsernamePasswordAuthenticationFilter(clientId, clientSecret, 
						scope,tokenUrl, refreshTokenUrl, checkTokenUrl,tokenRequestService());
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
	
	@Bean("userDetailsService")
	public UserDetailsService getUserDetailsService() {
		return new EmployeeDetailsService(clientId, clientSecret, 
				refreshTokenUrl, checkTokenUrl,tokenRequestService());
	}
	
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
}
