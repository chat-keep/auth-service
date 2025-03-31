package com.auth_service.config;

import org.springframework.context.annotation.Configuration;

/**
 * AwsSecretPropsConfig class. Used for mapping the AWS secret properties to a Java
 * object.
 */
@Configuration
public class AwsSecretPropsConfig {

	private String jwtSecret;

	public String getJwtSecret() {
		return jwtSecret;
	}

	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

}
