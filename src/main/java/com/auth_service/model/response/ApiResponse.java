package com.auth_service.model.response;

/**
 * ApiResponse class. Represents the response structure of the API.
 *
 * @param <T> The type of the data.
 */
public class ApiResponse<T> {

	private boolean success;

	private String message;

	private T data;

	private String errorCode;

	public ApiResponse(boolean success, String message, T data) {
		this(success, message, data, null);
	}

	public ApiResponse(boolean success, String message, T data, String errorCode) {
		this.success = success;
		this.message = message;
		this.data = data;
		this.errorCode = errorCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}