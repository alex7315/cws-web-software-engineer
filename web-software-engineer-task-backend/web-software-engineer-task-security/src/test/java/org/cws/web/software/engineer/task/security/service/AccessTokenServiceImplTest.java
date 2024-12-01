package org.cws.web.software.engineer.task.security.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.awaitility.Awaitility;
import org.cws.web.software.engineer.task.persistence.model.AccessToken;
import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.cws.web.software.engineer.task.security.test.configuration.DataJpaTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
      "cws.security.refresh.token.expiration.ms=50000",
      "cws.security.jwt.expiration.ms=5000",
      "cws.security.jwt.secret=Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65"
})
//@formatter:on
@ContextConfiguration(classes = { DataJpaTestConfiguration.class })
class AccessTokenServiceImplTest {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    private JdbcTemplate       jdbcTemplate;

    @Autowired
    private DataSource         dataSource;

    @Autowired
    private JwtHandler         jwtHandler;

    @BeforeEach
    void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    void shouldCreateAccessTokenAndPersistIt() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("user1");
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "credentials");
        AccessToken accessToken = accessTokenService.createAccessToken(auth);

        AccessToken actualAccessToken = findAccessToken(accessToken.getToken());

        assertThat(accessToken).usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withComparedFields("id", "token").build())
                .isEqualTo(actualAccessToken);
        //@formatter:on

    }

    private AccessToken findAccessToken(String tokenString) {
        //@formatter:off
        return  jdbcTemplate.queryForObject("select * from access_token where token = ?"
                , (rs, i) -> AccessToken
                                .builder()
                                .id(rs.getLong("id"))
                                .token(rs.getString("token"))
                                .build()
                , tokenString);
        //@formatter:on
    }

    @Test
    void shouldRevokeValidAccessToken() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("user1");
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "credentials");
        AccessToken accessToken = accessTokenService.createAccessToken(auth);

        assertThat(accessTokenService.validateAccessToken(accessToken.getToken())).isTrue();

        accessTokenService.revokeAccessToken(accessToken.getToken());

        assertThat(accessTokenService.validateAccessToken(accessToken.getToken())).isFalse();

    }

    @Test
    void shouldRevokeExpiredAccessToken() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("user1");
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "credentials");
        AccessToken accessToken = accessTokenService.createAccessToken(auth);

        assertThat(jwtHandler.validateJwtToken(accessToken.getToken())).isTrue();

        assertThat(accessTokenService.validateAccessToken(accessToken.getToken())).isTrue();

        Awaitility.await().atMost(50, TimeUnit.SECONDS).until(() -> !jwtHandler.validateJwtToken(accessToken.getToken()));

        assertThat(accessTokenService.validateAccessToken(accessToken.getToken())).isFalse();
    }

    @Test
    void shouldBeAbleToRevokeAccessTokenIsNotPresent() {
        assertThatCode(() -> accessTokenService.revokeAccessToken("NotExsistedToken")).doesNotThrowAnyException();
    }

}
