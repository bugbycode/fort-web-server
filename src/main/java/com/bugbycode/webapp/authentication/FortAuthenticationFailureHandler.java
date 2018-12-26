package com.bugbycode.webapp.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import com.util.exception.ImgCodeAuthenticationException;
import com.util.exception.PasswordAuthenticationException;

public class FortAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private String defaultFailureUrl;
	private boolean forwardToDestination = false;
	private boolean allowSessionCreation = true;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	public FortAuthenticationFailureHandler(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (defaultFailureUrl == null) {

			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Authentication Failed: " + exception.getMessage());
		}
		else {
			
			if(exception instanceof BadCredentialsException) {
				exception = new BadCredentialsException("用户名密码错误");
			}
			
			saveException(request, exception);

			if (forwardToDestination) {

				request.getRequestDispatcher(defaultFailureUrl)
						.forward(request, response);
			}
			else {
				redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
			}
		}
	}

	
	/**
	 * Caches the {@code AuthenticationException} for use in view rendering.
	 * <p>
	 * If {@code forwardToDestination} is set to true, request scope will be used,
	 * otherwise it will attempt to store the exception in the session. If there is no
	 * session and {@code allowSessionCreation} is {@code true} a session will be created.
	 * Otherwise the exception will not be stored.
	 */
	protected final void saveException(HttpServletRequest request,
			AuthenticationException exception) {
		if (forwardToDestination) {
			/*if (exception instanceof PasswordAuthenticationException) {
				request.setAttribute("PASSWORD_EXCEPTION", exception);
			} else if (exception instanceof ImgCodeAuthenticationException) {
				request.setAttribute("IMGCODE_EXCEPTION", exception);
			} else {*/
				request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
			//}
		} else {
			HttpSession session = request.getSession(false);
			if (session != null || allowSessionCreation) {
				/*if (exception instanceof PasswordAuthenticationException) {
					request.getSession().setAttribute("PASSWORD_EXCEPTION", exception);
					request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
					request.getSession().removeAttribute("IMGCODE_EXCEPTION");
					} else if (exception instanceof ImgCodeAuthenticationException) {
					request.getSession().setAttribute("IMGCODE_EXCEPTION", exception);
					request.getSession().removeAttribute("PASSWORD_EXCEPTION");
					request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
				} else {
					request.getSession().removeAttribute("IMGCODE_EXCEPTION");
					request.getSession().removeAttribute("PASSWORD_EXCEPTION");
					request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
				}*/
				request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
			}
		}
	}
	
	/**
	 * The URL which will be used as the failure destination.
	 *
	 * @param defaultFailureUrl the failure URL, for example "/loginFailed.jsp".
	 */
	public void setDefaultFailureUrl(String defaultFailureUrl) {
		Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl), "'"
				+ defaultFailureUrl + "' is not a valid redirect URL");
		this.defaultFailureUrl = defaultFailureUrl;
	}

	protected boolean isUseForward() {
		return forwardToDestination;
	}

	/**
	 * If set to <tt>true</tt>, performs a forward to the failure destination URL instead
	 * of a redirect. Defaults to <tt>false</tt>.
	 */
	public void setUseForward(boolean forwardToDestination) {
		this.forwardToDestination = forwardToDestination;
	}

	/**
	 * Allows overriding of the behaviour when redirecting to a target URL.
	 */
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	protected boolean isAllowSessionCreation() {
		return allowSessionCreation;
	}

	public void setAllowSessionCreation(boolean allowSessionCreation) {
		this.allowSessionCreation = allowSessionCreation;
	}

}
