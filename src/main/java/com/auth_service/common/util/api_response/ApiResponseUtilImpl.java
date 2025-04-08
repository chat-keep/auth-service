package com.auth_service.common.util.api_response;

import com.auth_service.model.response.ApiResponse;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseUtilImpl implements ApiResponseUtil {

	public static <T> ApiResponse<T> createSuccessResponse(String message, T data) {
		return new ApiResponse<>(true, message, data);
	}

	public static <T> ApiResponse<T> createErrorResponse(String message, T data) {
		return new ApiResponse<>(false, message, data);
	}

}