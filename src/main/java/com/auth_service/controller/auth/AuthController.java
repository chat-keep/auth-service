package com.auth_service.controller.auth;

import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.TokenRefreshRequest;
import com.auth_service.model.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * AuthController interface. Defines the endpoints for authenticating users.
 */
public interface AuthController {

	/**
	 * Authenticates a user and returns a JWT token.
	 * @param loginRequest the login request containing the user's identifier and password
	 * @return a ResponseEntity containing an ApiResponse with the JWT token
	 */
	ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest);

	/**
	 * Refreshes the JWT token using a refresh token.
	 * @param request the token refresh request containing the refresh token and username
	 * @return a ResponseEntity containing an ApiResponse with the new JWT token
	 */
	ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request);

}