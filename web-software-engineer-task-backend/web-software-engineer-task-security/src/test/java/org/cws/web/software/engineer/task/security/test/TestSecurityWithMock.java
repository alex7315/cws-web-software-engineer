package org.cws.web.software.engineer.task.security.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.cws.web.software.engineer.task.security.service.UserDetailsImpl;
import org.cws.web.software.engineer.task.security.test.configuration.SecurityTestConfiguration;
import org.cws.web.software.engineer.task.security.test.controller.SecurityTestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//@formatter:off
@WebMvcTest(controllers = {SecurityTestController.class}, properties = {
      "spring.datasource.url=jdbc:h2:mem:cws_github",
      "spring.datasource.driverClassName=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=password",
      "spring.jpa.defer-datasource-initialization=true",
      "spring.jpa.open-in-view=false",
      "spring.data.web.pageable.default-page-size=5",
      "cws.security.jwt.secret=Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65", 
      "cws.security.jwt.expiration.ms=3600000",
      "cws.security.refresh.token.expiration.ms=900000",
      "cws.security.session.timeout=60m"
})
//@formatter:on
@ContextConfiguration(classes = { SecurityTestConfiguration.class })
@ComponentScan({ "org.cws.web.software.engineer.task.security.test.controller" })
class TestSecurityWithMock {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtHandler jwtHandler;

    @Test
    void shouldRejectUnauthorizedRequestWithResponseContainsErrorMessage() throws Exception {
        //@formatter:off
        mockMvc.perform(get("/test").header("Authorization", "Bearer "
                + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkFsZXggS2x5Y2hrbyIsImlhdCI6MTUxNjIzOTAyMn0.YGGJe8E8JCRlWHMj6YipqAewQiHS6jmAcNWusVhfAl0"))
                .andExpect(status()
                               .isUnauthorized())
                .andExpect(MockMvcResultMatchers
                                .content()
                                .string("{\"message\": \"Authentication failed\"}"));
        //@formatter:on
    }

    @Test
    void shouldPerformRequestSuccessfully() throws Exception {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                new UserDetailsImpl(-1L, "user", "user@email.com", "password", Stream.of(new SimpleGrantedAuthority("ROLE_USER")).toList()), null);
        String jwt = jwtHandler.generateJwtToken(authentication);
        //@formatter:off
        mockMvc.perform(get("/test").header("Authorization", "Bearer "+ jwt)
                .header("Content-Type", "application/json"))
                .andExpect(status()
                               .isOk());
        //@formatter:on
    }
}
