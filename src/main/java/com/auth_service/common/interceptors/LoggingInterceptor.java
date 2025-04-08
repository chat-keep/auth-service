package com.auth_service.common.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

/**
 * LoggingInterceptor is a Spring interceptor that logs incoming HTTP requests and
 * outgoing responses.
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

	/**
	 * Logger instance for logging messages.
	 */
	private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

	/**
	 * This method is called before the request is processed. It logs the details of the
	 * incoming request.
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @param handler the handler that will process the request
	 * @return true to continue processing, false to abort
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		logRequestDetails(request);
		return true;
	}

	/**
	 * This method is called after the request has been processed but before the view is
	 * rendered. It logs the response status and any exceptions that occurred during
	 * processing.
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @param handler the handler that processed the request
	 * @param ex any exception that occurred during processing
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logResponseDetails(response);
		if (ex != null) {
			logger.error("Exception occurred: ", ex);
		}
	}

	/**
	 * Logs the details of the incoming HTTP request.
	 * @param request the HTTP request
	 */
	private void logRequestDetails(HttpServletRequest request) {
		logger.info("Incoming Request:");
		logger.info("Method: {}", request.getMethod());
		logger.info("URL: {}", request.getRequestURL());
		logHeaders(request);
	}

	/**
	 * Logs the headers of the HTTP request.
	 * @param request the HTTP request
	 */
	private void logHeaders(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null) {
			logger.info("Headers:");
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				logger.info("{}: {}", headerName, request.getHeader(headerName));
			}
		}
	}

	/**
	 * Logs the details of the HTTP response.
	 * @param response the HTTP response
	 */
	private void logResponseDetails(HttpServletResponse response) {
		logger.info("Response Status: {}", response.getStatus());
	}

}