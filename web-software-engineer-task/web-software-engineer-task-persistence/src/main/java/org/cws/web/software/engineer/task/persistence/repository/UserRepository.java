package org.cws.web.software.engineer.task.persistence.repository;

import java.util.Optional;

import org.cws.web.software.engineer.task.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
