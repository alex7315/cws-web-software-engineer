package org.cws.web.software.engineer.task.security.service;

import java.util.Set;

import org.cws.web.software.engineer.task.persistence.model.Role;
import org.cws.web.software.engineer.task.persistence.model.RoleEnum;
import org.cws.web.software.engineer.task.persistence.model.User;

public interface SecurityService {

    boolean isUserEmailExists(String email);

    boolean isUserNameExists(String username);

    void addRole(Set<Role> roles, RoleEnum role);

    void saveUser(User user);
}
