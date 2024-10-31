package org.cws.web.software.engineer.task.security.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.cws.web.software.engineer.task.persistence.model.Role;
import org.cws.web.software.engineer.task.persistence.model.RoleEnum;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class UserDetailsImplTest {

    @Test
    void shouldCreatesEqualUserDetails() {
        //@formatter:off
        User user = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@mail.de")
                .password("password")
                .roles(Stream.of(RoleEnum.ROLE_USER)
                                .map(r -> Role.builder().name(r).build())
                                .collect(Collectors.toSet()))
                .build();
        UserDetailsImpl userDetails1 = UserDetailsImpl.build(user);
        
        user.setRoles(Stream.of(RoleEnum.ROLE_USER, RoleEnum.ROLE_ADMIN)
                                .map(r -> Role.builder().name(r).build())
                                .collect(Collectors.toSet()));
        
        UserDetailsImpl userDetails2 = UserDetailsImpl.build(user);
        
        UserDetailsImpl expectedDetails = new UserDetailsImpl(user.getId()
                , user.getUsername()
                , user.getEmail()
                , user.getPassword()
                , user.getRoles()
                            .stream()
                            .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                            .toList());
        
        assertThat(userDetails2).usingRecursiveComparison().isEqualTo(expectedDetails);
        assertThat(userDetails1).isEqualTo(userDetails2);
    }
}
