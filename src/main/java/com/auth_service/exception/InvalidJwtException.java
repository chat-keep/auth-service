package com.auth_service.exception;// user.setRole(Role.USER);

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * InvalidJwtException class. Thrown when a JWT token is invalid.
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class InvalidJwtException extends RuntimeException {

}
