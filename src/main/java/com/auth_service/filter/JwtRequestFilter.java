package com.auth_service.filter;

import com.auth_service.exception.InvalidJwtException;
import com.auth_service.exception.InvalidSignatureException;
import com.auth_service.model.constants.ErrorCode;
import com.auth_service.model.constants.ErrorMessages;
import com.auth_service.model.response.ApiResponse;
import com.auth_service.common.util.api_response.ApiResponseUtil;
import com.auth_service.common.util.jwt.JwtUtilImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

		if (jwtToken != null) {
			if (!processToken(jwtToken, request, response)) {
				return;
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * Processes the JWT token.
	 * @param jwtToken the JWT token
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @return true if the token is valid, false otherwise
	 * @throws IOException if an I/O exception occurs
	 */
	private boolean processToken(String jwtToken, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			String username = jwtUtil.extractUsername(jwtToken);
			String role = jwtUtil.extractRole(jwtToken).name();

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (jwtUtil.validateToken(jwtToken, userDetails.getUsername())) {
					setAuthenticationForUser(request, userDetails, role);
				}
			}
			return true;
		}
		catch (IllegalArgumentException | InvalidJwtException e) {
			sendErrorResponse(response, ErrorMessages.INVALID_JWT_TOKEN);
		}
		catch (InvalidSignatureException e) {
			sendErrorResponse(response, ErrorMessages.INVALID_SIGNATURE);
		}
		catch (ExpiredJwtException e) {
			sendErrorResponse(response, ErrorMessages.EXPIRED_JWT_TOKEN);
		}
		return false;
	}

	/**
	 * Sends an error response to the client.
	 * @param response the HTTP response
	 * @param message the error message
	 * @throws IOException if an I/O exception occurs
	 */
	private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");
		ApiResponse<Void> apiResponse = ApiResponseUtil.createErrorResponse(message, null,
				ErrorCode.ERR_INVALID_JWT.getCode());
		ObjectMapper mapper = new ObjectMapper();
		response.getWriter().write(mapper.writeValueAsString(apiResponse));
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