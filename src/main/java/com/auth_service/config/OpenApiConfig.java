package com.auth_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the OpenAPI documentation for the application.
 */
@Configuration
public class OpenApiConfig {

	/**
	 * Configures the OpenAPI documentation for the application.
	 * @return an OpenAPI object
	 */
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Auth Service").version("1.0").description("API documentation for Auth Service"))
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("bearerAuth",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}

}