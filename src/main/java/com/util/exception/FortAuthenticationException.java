package com.util.exception;

import org.springframework.security.core.AuthenticationException;

public class FortAuthenticationException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7151078778533108738L;
	
	public FortAuthenticationException(String msg) {
		super(msg);
	}
}