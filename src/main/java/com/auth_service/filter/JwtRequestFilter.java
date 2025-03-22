package com.auth_service.filter;

import com.auth_service.util.jwt.JwtUtilImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JwtRequestFilter class. Filter that intercepts requests and validates JWT tokens.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

	private final JwtUtilImpl jwtUtil;

	private final UserDetailsService userDetailsService;

	public JwtRequestFilter(JwtUtilImpl jwtUtil, UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Filters the request and validates the JWT token.
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @param chain the filter chain
	 * @throws ServletException if a servlet exception occurs
	 * @throws IOException if an I/O exception occurs
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String jwtToken = extractJwtFromRequest(request);
		String username = null;
		String role = null;

		if (jwtToken != null) {
			try {
				username = jwtUtil.extractUsername(jwtToken);
				role = jwtUtil.extractRole(jwtToken).name();
				logger.info("Extracted Username: {}", username);
				logger.info("Extracted Role: {}", role);
			}
			catch (IllegalArgumentException e) {
				logger.error("Unable to get JWT Token", e);
			}
			catch (ExpiredJwtException e) {
				logger.warn("JWT Token has expired", e);
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (jwtUtil.validateToken(jwtToken, userDetails.getUsername())) {
				setAuthenticationForUser(request, userDetails, role);
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * Extracts the JWT token from the request.
	 * @param request the HTTP request
	 * @return the JWT token extracted from the request
	 */
	private String extractJwtFromRequest(HttpServletRequest request) {
		final String requestTokenHeader = request.getHeader("Authorization");
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			return requestTokenHeader.substring(7);
		}
		else {
			return null;
		}
	}

	/**
	 * Sets the authentication for the user.
	 * @param request the HTTP request
	 * @param userDetails the user details
	 * @param role the role of the user
	 */
	private void setAuthenticationForUser(HttpServletRequest request, UserDetails userDetails, String role) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
				null, Collections.singleton(() -> "ROLE_" + role));
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

}