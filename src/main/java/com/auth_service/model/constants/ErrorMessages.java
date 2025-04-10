package com.auth_service.model.constants;

/**
 * This class contains error messages.
 */
public class ErrorMessages {

	public static final String INVALID_JWT_TOKEN = "Failed with error: Invalid JWT token.";

	public static final String EXPIRED_JWT_TOKEN = "Failed with error: JWT token has expired.";

	public static final String ACCESS_DENIED = "Failed with error: Access denied.";

	public static final String USER_NOT_FOUND = "Failed with error: User not found.";

	public static final String PERSON_NOT_FOUND = "Failed with error: Person not found.";

	public static final String EMAIL_EXISTS = "Failed with error: Email already exists.";

	public static final String USERNAME_EXISTS = "Failed with error: Username already exists.";

	public static final String INVALID_CREDENTIALS = "Failed with error: Invalid credentials.";

	public static final String INVALID_SIGNATURE = "Failed with error: Invalid token signature.";

	public static final String UNKNOWN_ERROR = "Failed with error: An unexpected error occurred.";

	public static final String USERNAME_NOT_FOUND = "Failed with error: Username not found.";

	public static final String INVALID_AWS_SECRET_VALUE = "Failed with error: Invalid AWS secret value. Secret value is empty or not a number.";

	public static final String INVALID_AWS_SECRET_STRING = "Failed with error: Invalid AWS secret string.";

}