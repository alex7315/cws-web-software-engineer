package org.cws.web.software.engineer.task.security.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.cws.web.software.engineer.task.persistence.model.RefreshToken;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.cws.web.software.engineer.task.security.exception.TokenRefreshException;
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
    "spring.data.web.pageable.default-page-size=10",
    "cws.security.refresh.token.expiration.ms=1"
})
//@formatter:on
@ComponentScan({ "org.cws.web.software.engineer.task.security.service" })
@EnableJpaRepositories(basePackages = { "org.cws.web.software.engineer.task.persistence.repository" })
@EntityScan("org.cws.web.software.engineer.task.persistence.model")
class RefreshTokenServiceExpiredTokenTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private SecurityService     securityService;

    @Test
    void shouldFailToVerifyExpiredRefreshToken() {
        User user = User.builder().username("test_user").email("test_user@cws.de").password("password").build();
        securityService.saveUser(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        assertThrows(TokenRefreshException.class, () -> refreshTokenService.verifyExpiration(refreshToken));
        assertThat(refreshTokenService.findByToken(refreshToken.getToken())).isNotPresent();
    }
}