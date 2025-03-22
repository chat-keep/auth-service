package com.auth_service.controller.auth;

import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.TokenRefreshRequest;
import com.auth_service.model.response.ApiResponse;
import com.auth_service.service.auth.AuthService;
import com.auth_service.util.api_response.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

	private final AuthService authService;

	public AuthControllerImpl(AuthService authService) {
		this.authService = authService;
	}

	@Override
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest) {
		String token = authService.authenticate(loginRequest.getIdentifier(), loginRequest.getPassword());
		return ResponseEntity.ok(ApiResponseUtil.createSuccessResponse("Login successful", token));
	}

	@Override
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<String>> refreshToken(@RequestBody TokenRefreshRequest request) {
		String refreshToken = authService.refreshToken(request.getRefreshToken(), request.getUserName());
		return ResponseEntity.ok(ApiResponseUtil.createSuccessResponse("Token refreshed successfully", refreshToken));
	}

}