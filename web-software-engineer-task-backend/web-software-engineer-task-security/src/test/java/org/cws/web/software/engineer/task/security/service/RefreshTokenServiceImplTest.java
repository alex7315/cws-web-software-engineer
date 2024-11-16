package org.cws.web.software.engineer.task.security.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Optional;

import org.cws.web.software.engineer.task.persistence.model.RefreshToken;
import org.cws.web.software.engineer.task.persistence.model.User;
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
      "cws.security.refresh.token.expiration.ms=6000"
})
//@formatter:on
@ComponentScan({ "org.cws.web.software.engineer.task.security.service" })
@EnableJpaRepositories(basePackages = { "org.cws.web.software.engineer.task.persistence.repository" })
@EntityScan("org.cws.web.software.engineer.task.persistence.model")
class RefreshTokenServiceImplTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private SecurityService     securityService;

    @Test
    void shouldFindRefreshToken() {
        User user = User.builder().username("test_user").email("test_user@cws.de").password("password").build();
        securityService.saveUser(user);
        
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        Optional<RefreshToken> refreshTokenOptional = refreshTokenService.findByToken(refreshToken.getToken());
        assertThat(refreshTokenOptional).isPresent();

        Optional<RefreshToken> notExistedRefreshTokenOptional = refreshTokenService.findByToken("does_not_exist");
        assertThat(notExistedRefreshTokenOptional).isNotPresent();
    }

    @Test
    void shouldDeleteExistedToken() {
        User user = User.builder().username("test_user").email("test_user@cws.de").password("password").build();
        securityService.saveUser(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        refreshTokenService.deleteByUserId(user.getId());

        Optional<RefreshToken> deletedRefreshTokenOptional = refreshTokenService.findByToken(refreshToken.getToken());
        assertThat(deletedRefreshTokenOptional).isNotPresent();
    }

    @Test
    void shouldSuccessfullyDeleteTokenDoesNotExists() {
        User user = User.builder().username("test_user").email("test_user@cws.de").password("password").build();
        securityService.saveUser(user);

        assertThatCode(() -> refreshTokenService.deleteByUserId(user.getId())).doesNotThrowAnyException();
    }

    @Test
    void shouldVerifyValidRefreshToken() {
        User user = User.builder().username("test_user").email("test_user@cws.de").password("password").build();
        securityService.saveUser(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        //@formatter:off
        assertThat(refreshTokenService.verifyExpiration(refreshToken))
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "token", "expiryDate")
                .isEqualTo(refreshToken);
        //@formatter:on
    }

}
