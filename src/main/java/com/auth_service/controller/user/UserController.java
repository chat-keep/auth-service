package com.auth_service.controller.user;

import com.auth_service.model.entity.User;
import com.auth_service.model.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * UserController interface. Defines the endpoints for managing users.
 */
public interface UserController {

	/**
	 * Finds a user by ID.
	 * @param id the ID of the user to find
	 * @return a ResponseEntity containing an ApiResponse with the user entity
	 */
	ResponseEntity<ApiResponse<EntityModel<User>>> findUser(@PathVariable int id);

	/**
	 * Creates a new user.
	 * @param user the user entity to create
	 * @return a ResponseEntity containing an ApiResponse with the location of the created
	 * user
	 */
	ResponseEntity<ApiResponse<Void>> createUser(@Valid @RequestBody User user);

	/**
	 * Deletes a user by ID.
	 * @param id the ID of the user to delete
	 * @return a ResponseEntity containing an ApiResponse indicating the result of the
	 * operation
	 */
	ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable int id);

	/**
	 * Retrieves all users.
	 * @return a ResponseEntity containing an ApiResponse with the list of users
	 */
	ResponseEntity<ApiResponse<List<User>>> getAllUsers();

	/**
	 * Activates a user by ID.
	 * @param id the ID of the user to activate
	 * @return a ResponseEntity containing an ApiResponse indicating the result of the
	 * operation
	 */
	ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable int id);

}