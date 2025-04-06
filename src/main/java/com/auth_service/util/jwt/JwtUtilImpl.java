package com.auth_service.util.jwt;

import com.auth_service.exception.InvalidSignatureException;
import com.auth_service.exception.InvalidJwtException;
import com.auth_service.model.constants.Role;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
	private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000;

	/**
	 * The validity period of the refresh token in milliseconds.
	 */
	private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000;

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
		return createToken(claims, username, ACCESS_TOKEN_VALIDITY);
	}

	public String refreshToken(String username, Role role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", role.name());
		return createToken(claims, username, REFRESH_TOKEN_VALIDITY);
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
	 * Extracts all claims from the JWT token.
	 * @param token the JWT token
	 * @return the claims extracted from the token
	 */
	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
		}
		catch (SignatureException e) {
			throw new InvalidSignatureException();
		}
		catch (MalformedJwtException e) {
			throw new InvalidJwtException();
		}
	}

	/**
	 * Checks if the token is expired.
	 * @param token the JWT token
	 * @return true if the token is expired, false otherwise
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Creates a new JWT token.
	 * @param claims the claims to include in the token
	 * @param subject the subject of the token
	 * @param validity the validity period of the token
	 * @return the generated JWT token
	 */
	private String createToken(Map<String, Object> claims, String subject, long validity) {
		if (validity <= 0) {
			validity = ACCESS_TOKEN_VALIDITY;
		}

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + validity))
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
	}

}