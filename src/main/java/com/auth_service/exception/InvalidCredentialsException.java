package com.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * InvalidCredentialsException class. Thrown when the user provides invalid credentials.
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends RuntimeException {

}