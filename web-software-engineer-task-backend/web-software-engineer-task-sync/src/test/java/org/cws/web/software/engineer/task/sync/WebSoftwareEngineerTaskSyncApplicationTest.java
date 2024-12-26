package org.cws.web.software.engineer.task.sync;

import static org.assertj.core.api.Assertions.assertThat;

import org.cws.web.software.engineer.task.persistence.model.AccessToken;
import org.cws.web.software.engineer.task.security.service.AccessTokenService;
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
import org.springframework.test.annotation.DirtiesContext;

//@formatter:off
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:cws_github"
                , "spring.datasource.driverClassName=org.h2.Driver"
                , "spring.datasource.username=sa"
                , "spring.datasource.password=password"

                , "spring.jpa.defer-datasource-initialization=true"
                , "spring.jpa.open-in-view=false"
                , "spring.data.web.pageable.default-page-size=5"

                , "cws.github.sync.scheduled.rate=8"
                , "cws.github.user.page.size=4"
                , "cws.github.user.count.max=8"

                , "cws.security.jwt.secret=Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65" 
                , "cws.security.jwt.expiration.ms=600000"
                , "cws.security.refresh.token.expiration.ms=6000"
})
//@formatter:on
class WebSoftwareEngineerTaskSyncApplicationTest {

	private static final String ACTIVATE_URI = "/job/scheduler/activate";
	private static final String DEACTIVATE_URI = "/job/scheduler/deactivate";

    @Autowired
    private AccessTokenService  accessTokenService;

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	UserDetailsService userDetailsService;

	private HttpHeaders headers;

	@BeforeEach
	void init() {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
    @DirtiesContext
	void shouldActivateSyncJob() throws Exception {
		UserDetails adminDetails = userDetailsService.loadUserByUsername("admin");
		Authentication adminAuth = new UsernamePasswordAuthenticationToken(adminDetails, adminDetails.getAuthorities());
        AccessToken adminAuthToken = accessTokenService.createAccessToken(adminAuth);

        headers.setBearerAuth(adminAuthToken.getToken());

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(ACTIVATE_URI, HttpMethod.PUT, requestEntity,
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

	}

	@Test
    @DirtiesContext
	void shouldDeactivateSyncJob() throws Exception {
		UserDetails adminDetails = userDetailsService.loadUserByUsername("admin");
		Authentication adminAuth = new UsernamePasswordAuthenticationToken(adminDetails, adminDetails.getAuthorities());
        AccessToken adminAuthToken = accessTokenService.createAccessToken(adminAuth);

        headers.setBearerAuth(adminAuthToken.getToken());

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(DEACTIVATE_URI, HttpMethod.PUT, requestEntity,
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

	}

	@Test
    @DirtiesContext
	void shouldRejectRequestByUserWithoutAdminRole() throws Exception {
		UserDetails userDetails = userDetailsService.loadUserByUsername("user");
		Authentication userAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
        AccessToken userAuthToken = accessTokenService.createAccessToken(userAuth);

        headers.setBearerAuth(userAuthToken.getToken());

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(ACTIVATE_URI, HttpMethod.PUT, requestEntity,
				String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

	}
	
}
