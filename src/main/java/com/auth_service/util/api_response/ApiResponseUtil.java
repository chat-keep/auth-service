package com.auth_service.util.api_response;

import com.auth_service.model.response.ApiResponse;

/**
 * Interface for creating ApiResponse objects.
 */
public interface ApiResponseUtil {

	/**
	 * Creates a new ApiResponse object with the given parameters.
	 * @param <T> the type of the data field in the ApiResponse object
	 * @param message the message
	 * @param data the data
	 * @return a new ApiResponse object
	 */
	static <T> ApiResponse<T> createSuccessResponse(String message, T data) {
		return new ApiResponse<>(true, message, data);
	}

	/**
	 * Creates a new ApiResponse object with the given parameters.
	 * @param <T> the type of the data field in the ApiResponse object
	 * @param message the message
	 * @param data the data
	 * @return a new ApiResponse object
	 */
	static <T> ApiResponse<T> createErrorResponse(String message, T data, String errorCode) {
		return new ApiResponse<>(false, message, data, errorCode);
	}

}