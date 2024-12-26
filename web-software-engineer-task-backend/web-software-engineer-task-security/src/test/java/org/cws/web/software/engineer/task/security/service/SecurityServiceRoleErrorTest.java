package org.cws.web.software.engineer.task.security.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.cws.web.software.engineer.task.persistence.model.Role;
import org.cws.web.software.engineer.task.persistence.model.RoleEnum;
import org.cws.web.software.engineer.task.persistence.repository.RoleRepository;
import org.cws.web.software.engineer.task.persistence.repository.UserRepository;
import org.cws.web.software.engineer.task.security.exception.RoleNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class SecurityServiceRoleErrorTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    private SecurityServiceImpl securityServiceImpl;


    @Test
    void shouldThrowRoleNotFoundExceptionByUnknownRole() {
        securityServiceImpl = new SecurityServiceImpl(userRepository, roleRepository);
        when(roleRepository.findByName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.empty());

        Set<Role> roleSet = new HashSet<>();
        //@formatter:off
        assertThatThrownBy(() -> securityServiceImpl.addRole(roleSet, RoleEnum.ROLE_ADMIN))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("Role is not found");
        //@formatter:on
    }
}
