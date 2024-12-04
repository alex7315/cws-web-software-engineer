package org.cws.web.software.engineer.task.security.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.cws.web.software.engineer.task.persistence.model.AccessToken;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.cws.web.software.engineer.task.persistence.repository.UserRepository;
import org.cws.web.software.engineer.task.security.service.AccessTokenService;
import org.cws.web.software.engineer.task.security.service.UserDetailsImpl;
import org.cws.web.software.engineer.task.security.test.configuration.DataJpaTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    "cws.security.refresh.token.expiration.ms=600000",
    "cws.security.jwt.expiration.ms=60000",
    "cws.security.jwt.secret=Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65"
})
//@formatter:on
@ContextConfiguration(classes = { DataJpaTestConfiguration.class })
class AccessTokenLogoutHandlerTest {

    private AccessTokenLogoutHandler accessTokenLogoutHandler;

    @Autowired
    private AccessTokenService       accessTokenService;

    @Autowired
    private UserRepository           userRepository;

    @BeforeEach
    void init() {
        accessTokenLogoutHandler = new AccessTokenLogoutHandler(accessTokenService);
    }

    @Test
    void shouldRevokeAccessTokenByHandleOfLogout() {
        AccessToken accessToken = accessTokenService.createAccessToken("user1");
        assertThat(accessTokenService.validateAccessToken(accessToken.getToken())).isTrue();

        Request request = new Request(new Connector());
        org.apache.coyote.Request coyoteRequest = new org.apache.coyote.Request();
        coyoteRequest.getMimeHeaders().addValue("Authorization").setString("Bearer " + accessToken.getToken());
        request.setCoyoteRequest(coyoteRequest);

        User user1 = userRepository.findByUsername("user1").orElseThrow();
        Authentication auth = new UsernamePasswordAuthenticationToken(UserDetailsImpl.build(user1), accessToken.getToken());

        accessTokenLogoutHandler.logout(request, null, auth);

        assertThat(accessTokenService.validateAccessToken(accessToken.getToken())).isFalse();
    }

    @Test
    void shouldHandleLogautWithoutExceptionIfAccessTokenDoesNotSend() {
        Request request = new Request(new Connector());
        org.apache.coyote.Request coyoteRequest = new org.apache.coyote.Request();
        request.setCoyoteRequest(coyoteRequest);

        User user1 = userRepository.findByUsername("user1").orElseThrow();
        Authentication auth = new UsernamePasswordAuthenticationToken(UserDetailsImpl.build(user1), null);

        assertThatCode(() -> accessTokenLogoutHandler.logout(request, null, auth)).doesNotThrowAnyException();
    }
}
