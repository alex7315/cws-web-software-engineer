package org.cws.web.software.engineer.task.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

import org.cws.web.software.engineer.task.backend.dto.response.JwtResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

//@formatter:off
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
      "spring.datasource.url=jdbc:h2:mem:cws_github",
      "spring.datasource.driverClassName=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=password",
      "spring.jpa.defer-datasource-initialization=true",
      "spring.jpa.open-in-view=false",
      "spring.data.web.pageable.default-page-size=5",
      "cws.security.jwt.secret=Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65", 
      "cws.security.jwt.expiration.ms=3000",
      "cws.security.refresh.token.expiration.ms=9000",
      "cws.security.session.timeout=60m"
})
//@formatter:on
class WebSoftwareEngineerTaskBackendApplicationRefreshTokenExpiredTest {

	private static final String SIGNIN_URI = "/api/auth/signin";

	private static final String REFRESHTOKEN_URI = "/api/auth/refreshtoken";

	@Autowired
	TestRestTemplate restTemplate;

	private HttpHeaders headers;

	private JSONObject authJsonObject;

	@BeforeEach
	void init() throws Exception {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		authJsonObject = new JSONObject();
		authJsonObject.put("username", "user2");
		authJsonObject.put("password", "12345678");
	}

	@Test
	void shouldRefreshExpiredAccessTokenUsingRefreshToken() {
		// gets access token
		HttpEntity<String> request = new HttpEntity<>(authJsonObject.toString(), headers);
		ResponseEntity<JwtResponse> authResponse = restTemplate.postForEntity(SIGNIN_URI, request, JwtResponse.class);

		String refreshToken = authResponse.getBody().refreshToken();
		String authToken = authResponse.getBody().token();

		headers.setBearerAuth(authToken);
		HttpEntity<Void> usersRequestEntity = new HttpEntity<>(headers);

		//@formatter:off
        await().atMost(4, TimeUnit.SECONDS)
                .until(() -> restTemplate
                                .exchange("/api/users", HttpMethod.GET, usersRequestEntity, String.class)
                                .getStatusCode()
                                .isSameCodeAs(HttpStatusCode.valueOf(401))
                                );
        
        //@formatter:on

		HttpEntity<String> refreshTokenRequest = new HttpEntity<>("{\"refreshToken\": \"" + refreshToken + "\"}",
				headers);
		ResponseEntity<String> refreshTokenResponse = restTemplate.postForEntity(REFRESHTOKEN_URI, refreshTokenRequest,
				String.class);

		assertThat(refreshTokenResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		DocumentContext documentContext = JsonPath.parse(refreshTokenResponse.getBody());

		String actualRefreshToken = documentContext.read("$.refreshToken");
		assertThat(actualRefreshToken).isEqualTo(refreshToken);

		String actualToken = documentContext.read("$.token");
		assertThat(actualToken).isNotNull();

	}

	@Test
	void shouldRejectRefreshTokenRequestByExpiredRefreshToken() throws Exception {
		HttpEntity<String> request = new HttpEntity<>(authJsonObject.toString(), headers);
		ResponseEntity<JwtResponse> authResponse = restTemplate.postForEntity(SIGNIN_URI, request, JwtResponse.class);

		String refreshToken = authResponse.getBody().refreshToken();
		LoggerFactory.getLogger(this.getClass()).info("Auth request URI: {} refresh token: {}", SIGNIN_URI,
				refreshToken);

		HttpEntity<String> refreshTokenRequest = new HttpEntity<>("{\"refreshToken\": \"" + refreshToken + "\"}",
				headers);

		Thread.sleep(10000);
		HttpStatusCode statusCode = restTemplate.postForEntity(REFRESHTOKEN_URI, refreshTokenRequest, String.class)
				.getStatusCode();
		assertThat(statusCode).isEqualTo(HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()));

	}

}
