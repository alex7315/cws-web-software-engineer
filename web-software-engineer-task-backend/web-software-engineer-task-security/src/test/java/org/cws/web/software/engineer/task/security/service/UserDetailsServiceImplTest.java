package org.cws.web.software.engineer.task.security.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.cws.web.software.engineer.task.persistence.model.RoleEnum.ROLE_ADMIN;
import static org.cws.web.software.engineer.task.persistence.model.RoleEnum.ROLE_USER;

import org.cws.web.software.engineer.task.security.test.configuration.DataJpaTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;

//@formatter:off
@DataJpaTest(properties = {
      "spring.datasource.url=jdbc:h2:mem:cws_github",
      "spring.datasource.driverClassName=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=password",
      "spring.jpa.defer-datasource-initialization=true",
      "spring.jpa.open-in-view=false",
      "spring.data.web.pageable.default-page-size=10",
      "cws.security.refresh.token.expiration.ms=6000"
})
@ContextConfiguration(classes = {DataJpaTestConfiguration.class})
//@formatter:on
class UserDetailsServiceImplTest {

	@Autowired
	private UserDetailsService service;

	@Test
	void shouldReturnUserDetailsWithUserRole() throws Exception {
		UserDetails userAuthorizedDetail = service.loadUserByUsername("user1");

		assertThat(userAuthorizedDetail.getUsername()).isEqualTo("user1");
		assertThat(userAuthorizedDetail.getAuthorities().stream().map(ad -> ad.getAuthority()).toList())
				.containsExactly(ROLE_USER.name());
	}

	@Test
	void shouldReturnUserDetailsWithAdminAndUserRole() throws Exception {
		UserDetails userAuthorizedDetail = service.loadUserByUsername("user2");

		assertThat(userAuthorizedDetail.getUsername()).isEqualTo("user2");
		assertThat(userAuthorizedDetail.getAuthorities().stream().map(ad -> ad.getAuthority()).toList())
				.containsExactlyInAnyOrder(ROLE_USER.name(), ROLE_ADMIN.name());
	}

	@Test
    void shouldThrowUserNotFoundException() {

		assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(() -> {
			service.loadUserByUsername("unknown");
		}).withMessage("User not found. User name: unknown");
	}
}
