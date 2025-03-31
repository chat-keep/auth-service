package com.auth_service.model.dto;

import jakarta.validation.constraints.NotNull;

/**
 * TokenRefreshRequest class. Used for mapping the token refresh request JSON to a Java
 * object.
 */
public class TokenRefreshRequest {

	@NotNull(message = "Refresh token is required.")
	private String refreshToken;

	@NotNull(message = "Username is required.")
	private String userName;

	public TokenRefreshRequest() {
	}

	public TokenRefreshRequest(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "TokenRefreshRequest{" + "refreshToken='" + refreshToken + '\'' + ", userName='" + userName + '\'' + '}';
	}

}
