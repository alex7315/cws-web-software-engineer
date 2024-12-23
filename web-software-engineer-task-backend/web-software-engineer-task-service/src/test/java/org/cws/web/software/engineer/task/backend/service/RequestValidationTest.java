package org.cws.web.software.engineer.task.backend.service;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.stream.Stream;

import org.cws.web.software.engineer.task.backend.controller.AuthController;
import org.cws.web.software.engineer.task.backend.dto.request.LoginRequest;
import org.cws.web.software.engineer.task.persistence.model.AccessToken;
import org.cws.web.software.engineer.task.persistence.model.RefreshToken;
import org.cws.web.software.engineer.task.persistence.model.Role;
import org.cws.web.software.engineer.task.persistence.model.RoleEnum;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.cws.web.software.engineer.task.security.service.AccessTokenService;
import org.cws.web.software.engineer.task.security.service.RefreshTokenService;
import org.cws.web.software.engineer.task.security.service.UserDetailsImpl;
import org.cws.web.software.engineer.task.test.configuration.TestSecurityConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

//@formatter:off
@WebMvcTest(controllers = { AuthController.class }, properties = {
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
@ComponentScan({ "org.cws.web.software.engineer.task.backend.controller" })
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
//@formatter:on
class RequestValidationTest {

    private static final String      REFRESH_TOKEN_VALUE = "RefreshToken";
    private static final String      ACCESS_TOKEN_VALUE  = "AccessToken";
    private static final long        TEST_USER_ID = 100L;
    private static final String      SIGNIN_URI   = "/api/auth/signin";

    @Autowired
    private MockMvc               mockMvc;

    @MockBean
    private UserDetailsService       userDetailsService;

    @MockBean
    private UsersService          usersService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean(name = "delegatedAuthenticationEntryPoint")
    private AuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    @MockBean
    private AccessTokenService    accessTokenService;

    @MockBean
    private JwtHandler            jwtHandler;

    @MockBean
    private RefreshTokenService   refreshTokenService;


    @Test
    void shouldPerformCorrectAuthRequest() throws Exception {
        //@formatter:off
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                 .thenReturn(new UsernamePasswordAuthenticationToken(
                                 UserDetailsImpl
                                     .build(User.builder()
                                             .id(TEST_USER_ID)
                                             .username("testUser")
                                             .password("testPassword")
                                             .roles(Stream.of(Role.builder().name(RoleEnum.ROLE_USER).build()).collect(toSet()))
                                     .build())
                                 , ACCESS_TOKEN_VALUE));
        when(accessTokenService.createAccessToken(any(Authentication.class))).thenReturn(AccessToken
                                                                                             .builder()
                                                                                             .token(ACCESS_TOKEN_VALUE)
                                                                                             .build());
        when(refreshTokenService.createRefreshToken(TEST_USER_ID)).thenReturn(RefreshToken
                                                                                .builder()
                                                                                .expiryDate(Instant.now().plusSeconds(600L))
                                                                                .token(REFRESH_TOKEN_VALUE)
                                                                                .build());
        
        
        
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(LoginRequest
                                                                .builder()
                                                                .username("testUser")
                                                                .password("testPassword")
                                                                .build());
        
        mockMvc
            .perform(post(SIGNIN_URI).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", Matchers.is(ACCESS_TOKEN_VALUE)))
            .andExpect(jsonPath("$.type", Matchers.is("Bearer")))
            .andExpect(jsonPath("$.refreshToken", Matchers.is(REFRESH_TOKEN_VALUE)));
        //@formatter:on
        
    }

    @Test
    void shouldRejectAuthRequestWithEmptyUserName() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        //@formatter:off
        String requestBody = objectMapper.writeValueAsString(LoginRequest
                                                                .builder()
                                                                .username("")
                                                                .password("testPassword")
                                                                .build());
        mockMvc
            .perform(post(SIGNIN_URI).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", allOf(Matchers.containsString("loginRequest")
                                                , Matchers.containsString("Invalid username: Empty user name"))));
        //@formatter:on
    }

    @Test
    void shouldRejectAuthRequestWithEmptyUserNameAndEmptyPassword() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        //@formatter:off
        String requestBody = objectMapper.writeValueAsString(LoginRequest
                                                                .builder()
                                                                .password("")
                                                                .build());
        mockMvc
            .perform(post(SIGNIN_URI).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", allOf(Matchers.containsString("loginRequest")
                                                , Matchers.containsString("Invalid username: Empty user name")
                                                , Matchers.containsString("Invalid password: Empty password"))));
        //@formatter:on
    }
    
}
