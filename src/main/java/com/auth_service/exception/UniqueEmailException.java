package com.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * UniqueEmailException class. Thrown when a person tries to register with an email that
 * is already in use.
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class UniqueEmailException extends RuntimeException {

}
