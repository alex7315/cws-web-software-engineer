package org.cws.web.software.engineer.task.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.cws.web.software.engineer.task.persistence.model.RoleEnum;
import org.cws.web.software.engineer.task.security.service.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtHandlerTest {

    private final JwtHandler jwtHandler = new JwtHandler("Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65", 1000 * 30);
    private UserDetailsImpl  userDetails;

    @BeforeEach
    void init() {
        //@formatter:off
        userDetails = new UserDetailsImpl(1L
                            , "user1"
                            , "user1@mail.de"
                            , "password"
                            , Arrays.asList(new SimpleGrantedAuthority(RoleEnum.ROLE_USER.name())
                                            , new SimpleGrantedAuthority(RoleEnum.ROLE_ADMIN.name())));
        //@formatter:on
    }

    @Test
    void shouldCreateJwtContainsUserData() {
        
        Authentication auth = new TestingAuthenticationToken(userDetails, null);
        String token = jwtHandler.generateJwtToken(auth);
        String userName = jwtHandler.getUserNameFromJwtToken(token);

        assertThat(userName).isEqualTo("user1");
    }

    @Test
    void shouldValidateCreatesJwt() {
        Authentication auth = new TestingAuthenticationToken(userDetails, null);
        String token = jwtHandler.generateJwtToken(auth);

        assertThat(jwtHandler.validateJwtToken(token)).isTrue();
    }

    @Test
    void shouldFailJwtValidation() {
        assertThat(jwtHandler.validateJwtToken(
                "malformediJIUzM4NCJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTcyOTk0MjczMiwiZXhwIjoxNzI5OTQyNzQyfQ.AEVxgOsOzsMlUB_j3ogiRVmuiHtNfi8F9bk1USeH-XrFugvE341tI4UFxPmYgAzM"))
                        .isFalse();
        assertThat(jwtHandler.validateJwtToken("string")).isFalse();
        assertThat(jwtHandler.validateJwtToken(null)).isFalse();
        assertThat(jwtHandler.validateJwtToken("")).isFalse();
        assertThat(jwtHandler.validateJwtToken(
                "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTczMTc3NzEwNiwiZXhwIjoxNzMxNzc3MTM2fQ.SssMuzrvfmFe7FiORMLUvX77Dg0csgP6eufTxKEJIUYEIKBQ3aQVFLzk-maleformed"));

    }

    @Test
    void shouldInvalidateExpiredJwt() {
        JwtHandler jwtHandlerExpired = new JwtHandler("Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65", 1);
        Authentication auth = new TestingAuthenticationToken(userDetails, null);
        String token = jwtHandlerExpired.generateJwtToken(auth);

        await().atLeast(200, TimeUnit.SECONDS);

        assertThat(jwtHandlerExpired.validateJwtToken(token)).isFalse();
    }
}
