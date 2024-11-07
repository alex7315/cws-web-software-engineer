package org.cws.web.software.engineer.task.sync;

import static org.assertj.core.api.Assertions.assertThat;

import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

//@formatter:off
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
      "spring.datasource.url=jdbc:h2:mem:cws_github",
      "spring.datasource.driverClassName=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=password",
      "spring.jpa.defer-datasource-initialization=true",
      "spring.jpa.open-in-view=false",
      "spring.data.web.pageable.default-page-size=5",
      "cws.github.sync.scheduled.rate=8",
      "cws.github.user.page.size=4",
      "cws.github.user.count.max=8",
      "cws.security.jwt.secret=Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65", 
      "cws.security.jwt.expiration.ms=600000",
      "cws.github.authorization.token=token",
      "cws.security.refresh.token.expiration.ms=6000"
})
//@formatter:on
class WebSoftwareEngineerTaskSyncApplicationTest {

    private static final String ACTIVATE_URI   = "/job/scheduler/activate";
    private static final String DEACTIVATE_URI = "/job/scheduler/deactivate";

    @Autowired
    private JwtHandler          jwtHandler;

    @Autowired
    TestRestTemplate            restTemplate;

    @Autowired
    UserDetailsService          userDetailsService;

    private HttpHeaders         headers;


    @BeforeEach
    void init() throws Exception {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldActivateSyncJob() throws Exception {
        UserDetails adminDetails = userDetailsService.loadUserByUsername("admin");
        Authentication adminAuth = new UsernamePasswordAuthenticationToken(adminDetails, adminDetails.getAuthorities());
        String adminAuthToken = jwtHandler.generateJwtToken(adminAuth);

        headers.setBearerAuth(adminAuthToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(ACTIVATE_URI, HttpMethod.PUT, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    void shouldDeactivateSyncJob() throws Exception {
        UserDetails adminDetails = userDetailsService.loadUserByUsername("admin");
        Authentication adminAuth = new UsernamePasswordAuthenticationToken(adminDetails, adminDetails.getAuthorities());
        String adminAuthToken = jwtHandler.generateJwtToken(adminAuth);

        headers.setBearerAuth(adminAuthToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(DEACTIVATE_URI, HttpMethod.PUT, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    void shouldRejectRequestByUserWithoutAdminRole() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("user");
        Authentication userAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
        String userAuthToken = jwtHandler.generateJwtToken(userAuth);

        headers.setBearerAuth(userAuthToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(ACTIVATE_URI, HttpMethod.PUT, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }

}
