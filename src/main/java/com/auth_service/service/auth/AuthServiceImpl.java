package com.auth_service.service.auth;

import com.auth_service.exception.InvalidCredentialsException;
import com.auth_service.exception.UserNotFoundException;
import com.auth_service.model.entity.User;
import com.auth_service.repository.UserRepository;
import com.auth_service.common.util.jwt.JwtUtilImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceImpl implements AuthService {

	private final JwtUtilImpl jwtUtil;

	private final UserRepository userRepository;

	private final AuthenticationManager authenticationManager;

	public AuthServiceImpl(JwtUtilImpl jwtUtil, UserRepository userRepository,
			AuthenticationManager authenticationManager) {
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
	}

	public String authenticate(String identifier, String password) {
		User user = findUserByIdentifier(identifier);
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
		return jwtUtil.generateToken(identifier, user.getRole());
	}

	public String refreshToken(String refreshToken, String userName) {
		if (jwtUtil.validateRefreshToken(refreshToken, userName)) {
			String username = jwtUtil.extractUsername(refreshToken);
			User user = findUserByIdentifier(username);
			return jwtUtil.refreshToken(username, user.getRole());
		}
		else {
			throw new InvalidCredentialsException();
		}
	}

	/**
	 * Finds a user by email or username.
	 * @param identifier the email or username of the user to find
	 * @return the user entity
	 */
	private User findUserByIdentifier(String identifier) {
		User user = userRepository.findByPersonEmail(identifier);
		if (user == null) {
			user = userRepository.findByUserName(identifier);
		}
		if (user == null) {
			throw new UserNotFoundException();
		}
		return user;
	}

}