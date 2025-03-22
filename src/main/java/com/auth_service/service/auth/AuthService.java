package com.auth_service.service.auth;

/**
 * AuthService interface. Contains methods for authenticating and refreshing JWT tokens.
 */
public interface AuthService {

	/**
	 * Authenticates a user and returns a JWT token.
	 * @param identifier the user's identifier (email or username)
	 * @param password the user's password
	 * @return a JWT token
	 */
	String authenticate(String identifier, String password);

	/**
	 * Refreshes the JWT token using a refresh token.
	 * @param refreshToken the refresh token
	 * @param userName the username associated with the refresh token
	 * @return a new JWT token
	 */
	String refreshToken(String refreshToken, String userName);

}