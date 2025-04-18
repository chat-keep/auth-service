package com.auth_service.config;

import com.auth_service.filter.JwtRequestFilter;
import com.auth_service.model.constants.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * SecurityConfig class. Configures the security filter chain for the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	/**
	 * JwtRequestFilter object. Used for filtering JWT requests.
	 */
	private final JwtRequestFilter jwtRequestFilter;

	/**
	 * @description Constructor for SecurityConfig.
	 * @param jwtRequestFilter the JwtRequestFilter object
	 */
	@Autowired
	public SecurityConfig(@Lazy JwtRequestFilter jwtRequestFilter) {
		this.jwtRequestFilter = jwtRequestFilter;
	}

	/**
	 * @description Configures the security filter chain for the application.
	 * @param http the HttpSecurity object to configure
	 * @return a SecurityFilterChain object
	 * @throws Exception if an error occurs while configuring the security filter chain
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		configureCors(http);
		configureCsrf(http);
		configureAuthorization(http);
		configureSessionManagement(http);
		configureFilters(http);
		configureExceptionHandling(http);

		return http.build();
	}

	/**
	 * @description Configures CORS for the application.
	 * @param http the HttpSecurity object to configure
	 * @throws Exception if an error occurs while configuring the CORS
	 */
	private void configureCors(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
	}

	/**
	 * @description Configures CSRF for the application.
	 * @param http the HttpSecurity object to configure
	 * @throws Exception if an error occurs while configuring the CSRF
	 */
	private void configureCsrf(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
	}

	/**
	 * @description Configures authorization for the application.
	 * @param http the HttpSecurity object to configure
	 * @throws Exception if an error occurs while configuring the authorization
	 */
	private void configureAuthorization(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/auth/login", "/auth/refresh", "/v1/swagger-ui/**", "/v1/api-docs/**").permitAll();
			auth.requestMatchers(HttpMethod.POST, "/users").permitAll();
			auth.anyRequest().authenticated();
		});
	}

	/**
	 * @description Configures session management for the application.
	 * @param http the HttpSecurity object to configure
	 * @throws Exception if an error occurs while configuring the session management
	 */
	private void configureSessionManagement(HttpSecurity http) throws Exception {
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	}

	/**
	 * @description Configures filters for the application.
	 * @param http the HttpSecurity object to configure
	 * @throws Exception if an error occurs while configuring the filters
	 */
	private void configureFilters(HttpSecurity http) throws Exception {
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	/**
	 * @description Configures exception handling for the application.
	 * @param http the HttpSecurity object to configure
	 * @throws Exception if an error occurs while configuring the exception handling
	 */
	private void configureExceptionHandling(HttpSecurity http) throws Exception {
		http.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler()));
	}

	/**
	 * @description Configures CORS for the application.
	 * @return a UrlBasedCorsConfigurationSource object
	 */
	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = buildCorsConfiguration();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	/**
	 * @description Builds a CORS configuration object.
	 * @return a CorsConfiguration object
	 */
	private CorsConfiguration buildCorsConfiguration() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		return config;
	}

	/**
	 * @description Creates a BCrypt password encoder.accessDeniedException
	 * @return a BCryptPasswordEncoder object
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * @description Creates an AccessDeniedHandler object.
	 * @return an AccessDeniedHandler object
	 */
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, accessDeniedException) -> {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.getWriter().write(ErrorMessages.ACCESS_DENIED);
		};
	}

	/**
	 * @description Creates an AuthenticationManager object.
	 * @param authenticationConfiguration the AuthenticationConfiguration object
	 * @return an AuthenticationManager object
	 * @throws Exception if an error occurs while creating the AuthenticationManager
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}