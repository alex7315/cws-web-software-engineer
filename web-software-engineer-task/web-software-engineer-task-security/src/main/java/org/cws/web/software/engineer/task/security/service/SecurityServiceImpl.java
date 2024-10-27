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
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityServiceImpl implements SecurityService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    public SecurityServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserNameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = false)
    public void saveUser(User user) {
        userRepository.save(user);
    }

}
