package org.cws.web.software.engineer.task.security.service;

import java.util.Set;

import org.cws.web.software.engineer.task.persistence.model.Role;
import org.cws.web.software.engineer.task.persistence.model.RoleEnum;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.cws.web.software.engineer.task.persistence.repository.RoleRepository;
import org.cws.web.software.engineer.task.persistence.repository.UserRepository;
import org.cws.web.software.engineer.task.security.exception.RoleNotFoundException;
import org.cws.web.software.engineer.task.security.exception.SecurityException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    public SecurityServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean isUserEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isUserNameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void addRole(Set<Role> roles, RoleEnum role) {
        if (roles == null) {
            throw new SecurityException("Error to add role. Role set is null");
        }
        //@formatter:off
        Role foundRole = roleRepository
                                .findByName(role)
                                .orElseThrow(() -> new RoleNotFoundException("Role is not found."));
        
        roles.add(foundRole);
        //@formatter:on
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

}
