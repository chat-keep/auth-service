package com.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * InvalidSignatureException class. Thrown when the signature of a JWT token is invalid.
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class InvalidSignatureException extends RuntimeException {

}
