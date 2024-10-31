package org.cws.web.software.engineer.task.persistence.repository;

import java.util.Optional;

import org.cws.web.software.engineer.task.persistence.model.Role;
import org.cws.web.software.engineer.task.persistence.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);
}
