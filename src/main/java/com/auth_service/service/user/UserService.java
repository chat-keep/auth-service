package com.auth_service.service.user;

import com.auth_service.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * UserService interface. Defines the methods that the UserService class must implement.
 */
public interface UserService extends UserDetailsService {

	/**
	 * Finds a user by ID.
	 * @param id the ID of the user to find
	 * @return the user entity
	 */
	User findById(int id);

	/**
	 * Creates a new user.
	 * @param user the user entity to create
	 * @return the created user entity
	 */
	User save(User user);

	/**
	 * Deletes a user by ID.
	 * @param id the ID of the user to delete
	 */
	void deleteById(int id);

	/**
	 * Retrieves all users.
	 * @return the list of users
	 */
	List<User> findAll();

	/**
	 * Activates a user by ID.
	 * @param id the ID of the user to activate
	 * @return a message indicating the result of the operation
	 */
	String activateUser(int id);

}