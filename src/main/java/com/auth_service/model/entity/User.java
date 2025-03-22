package com.auth_service.model.entity;

import com.auth_service.model.constants.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * User class. Represents a user.
 */
@Entity(name = "user_info")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@Size(min = 2)
	@NotNull(message = "User name is required.")
	@JsonProperty("user_name")
	private String userName;

	@Size(min = 2)
	@NotNull(message = "Password is required.")
	@JsonProperty("password")
	private String password;

	@OneToOne(cascade = CascadeType.ALL)
	@Valid
	@NotNull(message = "Person information is required.")
	private Person person;

	@NotNull
	private Boolean active = false;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private Role role = Role.USER;

	public User() {
	}

	public User(Long id, String userName, String password, Person person, Boolean active, Role role) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.person = person;
		this.active = active;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", userName='" + userName + '\'' + ", password='" + password + '\'' + ", person="
				+ person + ", active=" + active + ", role='" + role + '\'' + '}';
	}

}