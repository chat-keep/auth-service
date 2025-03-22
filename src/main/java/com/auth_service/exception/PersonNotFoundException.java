package com.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * PersonNotFoundException class. Thrown when a person is not found in the database.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PersonNotFoundException extends RuntimeException {

}
