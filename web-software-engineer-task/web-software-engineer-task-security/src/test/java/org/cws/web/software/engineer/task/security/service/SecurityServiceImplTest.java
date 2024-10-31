package org.cws.web.software.engineer.task.security.service;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cws.web.software.engineer.task.persistence.model.RoleEnum.ROLE_ADMIN;
import static org.cws.web.software.engineer.task.persistence.model.RoleEnum.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import java.util.stream.Stream;

import org.cws.web.software.engineer.task.persistence.model.Role;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.cws.web.software.engineer.task.security.exception.SecurityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@formatter:off
@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:cws_github",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.defer-datasource-initialization=true",
        "spring.jpa.open-in-view=false",
        "spring.data.web.pageable.default-page-size=10"
  })
//@formatter:on
@ComponentScan({ "org.cws.web.software.engineer.task.security.service" })
@EnableJpaRepositories(basePackages = { "org.cws.web.software.engineer.task.persistence.repository" })
@EntityScan("org.cws.web.software.engineer.task.persistence.model")
class SecurityServiceImplTest {

    @Autowired
    private SecurityService securityService;

    @Test
    void shouldFindUserByUsername() {
        assertThat(securityService.isUserNameExists("user1")).isTrue();
    }

    @Test
    void shouldNotFindUser() {
        assertThat(securityService.isUserNameExists("unknown")).isFalse();
    }

    @Test
    void shouldNotFindByNullUserName() {
        assertThat(securityService.isUserNameExists(null)).isFalse();
    }

    @Test
    void shouldFindUserByEmail() {
        assertThat(securityService.isUserEmailExists("user1@mail.de")).isTrue();
    }

    @Test
    void shouldNotFindUserByEmail() {
        assertThat(securityService.isUserEmailExists("unknown@web.de")).isFalse();
    }

    @Test
    void shouldNotFindByNullEmail() {
        assertThat(securityService.isUserEmailExists(null)).isFalse();
    }

    @Test
    void shouldAddsExistedRole() {
        Set<Role> roles = Stream.of(Role.builder().name(ROLE_ADMIN).build()).collect(toSet());
        securityService.addRole(roles, ROLE_USER);
        assertThat(roles.stream().map(Role::getName).toList()).containsExactlyInAnyOrder(ROLE_ADMIN, ROLE_USER);
    }

    @Test
    void shouldThrowExceptionWithNullRoleSet() {
        assertThrows(SecurityException.class, () -> securityService.addRole(null, ROLE_USER));
    }

    @Test
    void shouldSaveCreatedUser() throws Exception {
        //@formatter:off
        Set<Role> roles = Stream.of(Role.builder()
                                        .name(ROLE_ADMIN)
                                        .build())
                .collect(toSet());
        User userToSave = User.builder()
                    .email("new_email@web.de")
                    .password("password")
                    .username("new_user")
                    .roles(roles)
                    .build();
        //@formatter:on
        
        securityService.saveUser(userToSave);
        
        assertThat(securityService.isUserNameExists("new_user")).isTrue();
        
    }

}
