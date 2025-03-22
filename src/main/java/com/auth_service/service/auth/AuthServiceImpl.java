package com.auth_service.service.auth;

import com.auth_service.exception.InvalidCredentialsException;
import com.auth_service.exception.UserNotFoundException;
import com.auth_service.model.entity.User;
import com.auth_service.repository.UserRepository;
import com.auth_service.util.jwt.JwtUtilImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceImpl implements AuthService {

	private final JwtUtilImpl jwtUtil;

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder;

	public AuthServiceImpl(JwtUtilImpl jwtUtil, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public String authenticate(String identifier, String password) {
		User user = findUserByIdentifier(identifier);
		validatePassword(password, user.getPassword());
		return jwtUtil.generateToken(identifier, user.getRole());
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
	 * Validates the user's password.
	 * @param rawPassword the raw password
	 * @param encodedPassword the encoded password
	 */
	private void validatePassword(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new InvalidCredentialsException();
		}
	}

}