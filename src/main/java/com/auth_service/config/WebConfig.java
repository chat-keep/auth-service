package com.auth_service.config;

import com.auth_service.common.interceptors.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig class to register interceptors.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * The LoggingInterceptor instance to be registered.
	 */
	private final LoggingInterceptor loggingInterceptor;

	/**
	 * Constructor to inject the LoggingInterceptor.
	 * @param loggingInterceptor the LoggingInterceptor to be injected
	 */
	@Autowired
	public WebConfig(LoggingInterceptor loggingInterceptor) {
		this.loggingInterceptor = loggingInterceptor;
	}

	/**
	 * Method to add interceptors to the registry.
	 * @param registry the InterceptorRegistry to add interceptors to
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggingInterceptor);
	}

}