package com.util.exception;

import org.springframework.security.core.AuthenticationException;

public class PasswordAuthenticationException extends AuthenticationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7151078778533108738L;
	
	public PasswordAuthenticationException(String msg) {
		super(msg);
	}
}