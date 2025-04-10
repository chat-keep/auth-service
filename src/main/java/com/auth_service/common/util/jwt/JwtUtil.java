package com.auth_service.common.util.jwt;

import com.auth_service.model.constants.Role;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.function.Function;

public interface JwtUtil {

	/**
	 * @description Extracts the username from the JWT token.
	 * @param token the JWT token
	 * @return the username extracted from the token
	 */
	String extractUsername(String token);

	/**
	 * @description Extracts the expiration date from the JWT token.
	 * @param token the JWT token
	 * @return the expiration date extracted from the token
	 */
	Date extractExpiration(String token);

	/**
	 * @description Extracts a specific claim from the JWT token using a claims resolver
	 * function.
	 * @param <T> the type of the claim
	 * @param token the JWT token
	 * @param claimsResolver the function to resolve the claim
	 * @return the claim extracted from the token
	 */
	<T> T extractClaim(String token, Function<Claims, T> claimsResolver);

	/**
	 * @description Generates a new JWT token for the given username and role.
	 * @param username the username for which the token is generated
	 * @param role the role of the user
	 * @return the generated JWT token
	 */
	String generateToken(String username, Role role);

	/**
	 * @description Generates a new refresh token for the given username and role.
	 * @param username the username for which the refresh token is generated
	 * @param role the role of the user
	 * @return the generated refresh token
	 */
	String refreshToken(String username, Role role);

	/**
	 * @description Validates the JWT token against the given username.
	 * @param token the JWT token
	 * @param username the username to validate against
	 * @return true if the token is valid, false otherwise
	 */
	boolean validateToken(String token, String username);

	/**
	 * @description Validates the refresh token against the given username.
	 * @param token the refresh token
	 * @param username the username to validate against
	 * @return true if the refresh token is valid, false otherwise
	 */
	boolean validateRefreshToken(String token, String username);

	/**
	 * @description Extracts the role from the JWT token.
	 * @param token the JWT token
	 * @return the role extracted from the token
	 */
	Role extractRole(String token);

}