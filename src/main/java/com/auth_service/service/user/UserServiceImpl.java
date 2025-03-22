package com.auth_service.service.user;

import com.auth_service.exception.PersonNotFoundException;
import com.auth_service.exception.UniqueEmailException;
import com.auth_service.exception.UniqueUserNameException;
import com.auth_service.exception.UserNotFoundException;
import com.auth_service.model.constants.Role;
import com.auth_service.model.entity.User;
import com.auth_service.repository.PersonRepository;
import com.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final PersonRepository personRepository;

	private final BCryptPasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PersonRepository personRepository,
			BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.personRepository = personRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User findById(int id) {
		User currentUser = getCurrentUser();
		validateAccess(currentUser, id);

		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		validateUser(user);

		return user;
	}

	/**
	 * Retrieves the current logged user.
	 * @return the current user
	 */
	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userRepository.findByUserName(userDetails.getUsername());
	}

	/**
	 * Validates if the current user has access to the user with the given ID.
	 * @param currentUser the current user
	 * @param userId the ID of the user to validate access
	 */
	private void validateAccess(User currentUser, int userId) {
		boolean isAdmin = currentUser.getRole() == Role.ADMIN;

		if (!isAdmin && currentUser.getId() != userId) {
			throw new AccessDeniedException("Access is denied");
		}
	}

	/**
	 * Validates if the user has a person associated.
	 * @param user the user to validate
	 */
	private void validateUser(User user) {
		if (user.getPerson() == null) {
			throw new PersonNotFoundException();
		}
	}

	@Transactional
	public User save(User user) {
		if (user.getPerson() == null) {
			throw new PersonNotFoundException();
		}

		if (emailExists(user.getPerson().getEmail())) {
			throw new UniqueEmailException();
		}

		if (userNameExists(user.getUserName())) {
			throw new UniqueUserNameException();
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if (user.getRole() == null) {
			// user.setRole(Role.USER);
			user.setRole(Role.ADMIN);
		}
		if (user.getActive() == null) {
			user.setActive(true);
		}

		personRepository.save(user.getPerson());
		return userRepository.save(user);
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteById(int id) {
		User user = userRepository.findById(id).orElse(null);

		if (user == null) {
			throw new UserNotFoundException();
		}

		if (user.getPerson() == null) {
			throw new PersonNotFoundException();
		}

		personRepository.deleteById(user.getPerson().getId());
		userRepository.deleteById(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public List<User> findAll() {
		List<User> users = userRepository.findAll();

		if (users.isEmpty()) {
			throw new UserNotFoundException();
		}

		return userRepository.findAll();
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public String activateUser(int id) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

		if (user.getActive()) {
			return "User is already active";
		}

		user.setActive(true);
		userRepository.save(user);
		return "User activated";
	}

	/**
	 * Checks if an email already exists.
	 * @param email the email to check
	 * @return true if the email exists, false otherwise
	 */
	private boolean emailExists(String email) {
		return userRepository.existsByPersonEmail(email);
	}

	/**
	 * Checks if a userName already exists.
	 * @param userName the userName to check
	 * @return true if the userName exists, false otherwise
	 */
	private boolean userNameExists(String userName) {
		return userRepository.existsByUserName(userName);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with userName: " + userName);
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				new ArrayList<>());
	}

}