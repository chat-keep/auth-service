package com.auth_service.repository;

import com.auth_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository interface. Extends JpaRepository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByPersonEmail(String email);

	User findByUserName(String userName);

	boolean existsByPersonEmail(String email);

	boolean existsByUserName(String userName);

}
