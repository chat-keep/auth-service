package com.auth_service.exception;

import com.auth_service.model.constants.ErrorCode;
import com.auth_service.model.constants.ErrorMessages;
import com.auth_service.model.response.ApiResponse;
import com.auth_service.util.api_response.ApiResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * CustomExceptionHandler class. Handles exceptions thrown by the application.
 */
@ControllerAdvice
public class CustomExceptionHandler {

	/**
	 * Handles the AccessDeniedException.
	 * @param ex the exception
	 * @return a ResponseEntity containing an ApiResponse with the error message
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
		ApiResponse<Void> response = ApiResponseUtil.createErrorResponse(ErrorMessages.ACCESS_DENIED, null,
				ErrorCode.ERR_ACCESS_DENIED.getCode());
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	/**
	 * Handles the UserNotFoundException.
	 * @param ex the exception
	 * @return a ResponseEntity containing an ApiResponse with the error message
	 */
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException ex) {
		ApiResponse<Void> response = ApiResponseUtil.createErrorResponse(ErrorMessages.USER_NOT_FOUND, null,
				ErrorCode.ERR_USER_NOT_FOUND.getCode());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles the PersonNotFoundException.
	 * @param ex the exception
	 * @return a ResponseEntity containing an ApiResponse with the error message
	 */
	@ExceptionHandler(PersonNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiResponse<Void>> handlePersonNotFoundException(PersonNotFoundException ex) {
		ApiResponse<Void> response = ApiResponseUtil.createErrorResponse(ErrorMessages.PERSON_NOT_FOUND, null,
				ErrorCode.ERR_PERSON_NOT_FOUND.getCode());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles the general exception.
	 * @param ex the exception
	 * @return a ResponseEntity containing an ApiResponse with the error message
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
		ApiResponse<Void> response = ApiResponseUtil.createErrorResponse(ErrorMessages.UNKNOWN_ERROR, null,
				ErrorCode.ERR_INTERNAL_SERVER.getCode());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles the UniqueEmailException.
	 * @param ex the exception
	 * @return a ResponseEntity containing an ApiResponse with the error message
	 */
	@ExceptionHandler(UniqueEmailException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ApiResponse<Void>> handleUniqueEmailException(UniqueEmailException ex) {
		ApiResponse<Void> response = ApiResponseUtil.createErrorResponse(ErrorMessages.EMAIL_EXISTS, null,
				ErrorCode.ERR_EMAIL_EXISTS.getCode());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	/**
	 * Handles the UniqueUserNameException.
	 * @param ex the exception
	 * @return a ResponseEntity containing an ApiResponse with the error message
	 */
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(UniqueUserNameException.class)
	public ResponseEntity<ApiResponse<Void>> handleUniqueUserNameException(UniqueUserNameException ex) {
		ApiResponse<Void> response = ApiResponseUtil.createErrorResponse(ErrorMessages.USERNAME_EXISTS, null,
				ErrorCode.ERR_USERNAME_EXISTS.getCode());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	/**
	 * Handles the InvalidCredentialsException.
	 * @param ex the exception
	 * @return a ResponseEntity containing an ApiResponse with the error message
	 */
	@ExceptionHandler(InvalidCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ApiResponse<Void>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
		ApiResponse<Void> response = ApiResponseUtil.createErrorResponse(ErrorMessages.INVALID_CREDENTIALS, null,
				ErrorCode.ERR_INVALID_CREDENTIALS.getCode());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handles the UsernameNotFoundException.
	 * @param ex the exception
	 * @return a ResponseEntity containing an ApiResponse with the error message
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
		ApiResponse<Void> response = ApiResponseUtil.createErrorResponse(ErrorMessages.USER_NOT_FOUND, null,
				ErrorCode.ERR_USER_NOT_FOUND.getCode());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles the InvalidTokenException.
	 * @param ex the exception
	 * @return a ResponseEntity containing an ApiResponse with the error message
	 */
	@ExceptionHandler(InvalidSignatureException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ApiResponse<Void>> handleInvalidSignatureException(InvalidSignatureException ex) {
		ApiResponse<Void> response = ApiResponseUtil.createErrorResponse(ErrorMessages.INVALID_SIGNATURE, null,
				ErrorCode.ERR_INVALID_SIGNATURE.getCode());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handles the ExpiredJwtException.
	 * @param ex the exception
	 * @return a ResponseEntity containing an ApiResponse with the error message
	 */
	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ApiResponse<Void>> handleExpiredJwtException(ExpiredJwtException ex) {
		ApiResponse<Void> response = ApiResponseUtil.createErrorResponse(ErrorMessages.EXPIRED_JWT_TOKEN, null,
				ErrorCode.ERR_EXPIRED_JWT.getCode());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

}