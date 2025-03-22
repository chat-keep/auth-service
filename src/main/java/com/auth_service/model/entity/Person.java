package com.auth_service.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Person class. Represents a person.
 */
@Entity
public class Person {

	@Id
	@GeneratedValue
	private Long id;

	@Size(min = 2, message = "Invalid first name.")
	@NotNull(message = "First name is required.")
	@JsonProperty("first_name")
	private String firstName;

	@Size(min = 2, message = "Invalid last name.")
	@NotNull(message = "Last name is required.")
	@JsonProperty("last_name")
	private String lastName;

	@Email(message = "Invalid Email.")
	@Size(min = 2, message = "Min 2.")
	@NotNull(message = "Email is required.")
	@JsonProperty("email")
	private String email;

	@Size(min = 2, message = "Invalid phone number.")
	@NotNull(message = "Phone number is required.")
	@JsonProperty("phone")
	private String phone;

	@OneToOne(mappedBy = "person")
	@JsonIgnore
	private User user;

	public Person() {
	}

	public Person(Long id, String firstName, String lastName, String email, String phone) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Person{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
				+ ", email='" + email + '\'' + ", phone='" + phone + '\'' + '}';
	}

}
