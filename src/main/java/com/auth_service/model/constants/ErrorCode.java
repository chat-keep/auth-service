package com.auth_service.model.constants;

/**
 * ErrorCode enum. Contains the error codes used in the API.
 */
public enum ErrorCode {

	ERR_ACCESS_DENIED("AUTH_001"), ERR_USER_NOT_FOUND("USR_001"), ERR_PERSON_NOT_FOUND("USR_002"), ERR_INTERNAL_SERVER(
			"GEN_001"), ERR_EMAIL_EXISTS("USR_003"), ERR_USERNAME_EXISTS("USR_004"), ERR_INVALID_CREDENTIALS(
					"AUTH_002"), ERR_USERNAME_NOT_FOUND("USR_005"), ERR_INVALID_SIGNATURE("AUTH_003");

	private final String code;

	ErrorCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}