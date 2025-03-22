package com.auth_service.controller.health;

import com.auth_service.model.response.ApiResponse;
import com.auth_service.util.api_response.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthControllerImpl implements HealthController {

	@GetMapping("/health")
	public ResponseEntity<ApiResponse<String>> healthCheck() {
		return ResponseEntity.ok(ApiResponseUtil.createSuccessResponse("Application is running", "OK"));
	}

}