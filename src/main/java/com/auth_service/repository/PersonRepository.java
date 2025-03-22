package com.auth_service.repository;

import com.auth_service.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PersonRepository interface. Extends JpaRepository.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
