package com.auth_service.controller.health;

import com.auth_service.model.response.ApiResponse;
import org.springframework.http.ResponseEntity;

/**
 * HealthController interface. Defines the endpoint for checking the health of the
 * application.
 */
public interface HealthController {

	/**
	 * Checks the health of the application.
	 * @return a ResponseEntity containing an ApiResponse with the health status
	 */
	ResponseEntity<ApiResponse<String>> healthCheck();

}