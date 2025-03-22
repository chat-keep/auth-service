package com.auth_service.controller.user;

import com.auth_service.model.entity.User;
import com.auth_service.model.response.ApiResponse;
import com.auth_service.service.user.UserService;
import com.auth_service.util.api_response.ApiResponseUtil;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

	private final UserService userService;

	public UserControllerImpl(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<EntityModel<User>>> findUser(@PathVariable int id) {
		User user = userService.findById(id);
		EntityModel<User> userModel = EntityModel.of(user);
		return ResponseEntity.ok(ApiResponseUtil.createSuccessResponse("User retrieved", userModel));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createUser(@Valid @RequestBody User user) {
		User savedUser = userService.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).body(ApiResponseUtil.createSuccessResponse("User created", null));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable int id) {
		userService.deleteById(id);
		return ResponseEntity.ok(ApiResponseUtil.createSuccessResponse("User deleted", null));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
		List<User> users = userService.findAll();
		return ResponseEntity.ok(ApiResponseUtil.createSuccessResponse("Users retrieved", users));
	}

	@PutMapping("/{id}/activate")
	public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable int id) {
		String message = userService.activateUser(id);
		return ResponseEntity.ok(ApiResponseUtil.createSuccessResponse("User activated", null));
	}

}