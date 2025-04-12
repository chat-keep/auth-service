package com.auth_service.common.util.jwt;

import com.auth_service.exception.InvalidAwsSecretValueException;
import com.auth_service.exception.InvalidSignatureException;
import com.auth_service.exception.InvalidJwtException;
import com.auth_service.model.constants.Role;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
@DependsOn("awsSecretsManagerConfig")
public class JwtUtilImpl implements JwtUtil {

	/**
	 * The secret key used for signing the JWT tokens. It is fetched from the system
	 * properties.
	 */
	private final String JWT_SECRET = System.getProperty("JWT_SECRET");

	/**
	 * The validity period of the access token in milliseconds.
	 */
	private final String ACCESS_TOKEN_VALIDITY = System.getProperty("AUTH_SERVICE_ACCESS_TOKEN_VALIDITY");

	/**
	 * The validity period of the refresh token in milliseconds.
	 */
	private final String REFRESH_TOKEN_VALIDITY = System.getProperty("AUTH_SERVICE_REFRESH_TOKEN_VALIDITY");

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws MalformedJwtException {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(String username, Role role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", role.name());
		return createToken(claims, username, getAccessTokenValidity());
	}

	public String refreshToken(String username, Role role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", role.name());
		return createToken(claims, username, getRefreshTokenValidity());
	}

	public boolean validateToken(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(username) && !isTokenExpired(token));
	}

	public boolean validateRefreshToken(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return extractedUsername.equals(username);
	}

	public Role extractRole(String token) {
		return Role.valueOf(extractClaim(token, claims -> claims.get("role", String.class)));
	}

	/**
	 * @description Gets the access token validity period from the system properties.
	 * @return the access token validity period in milliseconds
	 */
	private long getAccessTokenValidity() {
		try {
			return Long.parseLong(ACCESS_TOKEN_VALIDITY);
		}
		catch (NumberFormatException e) {
			throw new InvalidAwsSecretValueException();
		}
	}

	/**
	 * @description Gets the refresh token validity period from the system properties.
	 * @return the refresh token validity period in milliseconds
	 */
	private long getRefreshTokenValidity() {
		try {
			return Long.parseLong(REFRESH_TOKEN_VALIDITY);
		}
		catch (NumberFormatException e) {
			throw new InvalidAwsSecretValueException();
		}
	}

	/**
	 * @description Extracts all claims from the JWT token.
	 * @param token the JWT token
	 * @return the claims extracted from the token
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	/**
	 * @description Checks if the token is expired.
	 * @param token the JWT token
	 * @return true if the token is expired, false otherwise
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * @description Creates a new JWT token.
	 * @param claims the claims to include in the token
	 * @param subject the subject of the token
	 * @param validity the validity period of the token
	 * @return the generated JWT token
	 */
	private String createToken(Map<String, Object> claims, String subject, long validity) {
		if (validity <= 0) {
			validity = getAccessTokenValidity();
		}

		validity = validity * 1000;

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + validity))
				.signWith(getSignInKey(), SignatureAlgorithm.HS512).compact();
	}

	/**
	 * @description Gets the signing key for the JWT token.
	 * @return the signing key
	 */
	private Key getSignInKey() {
		byte[] keyBytes = Base64.getDecoder().decode(JWT_SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}