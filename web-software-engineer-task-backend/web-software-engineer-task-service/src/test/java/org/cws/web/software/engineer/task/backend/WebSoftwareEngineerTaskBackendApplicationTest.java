package org.cws.web.software.engineer.task.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.cws.web.software.engineer.task.backend.dto.response.JwtResponse;
import org.json.JSONObject;
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
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

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
		"cws.security.jwt.expiration.ms=600000",
		"cws.security.refresh.token.expiration.ms=6000000",
	    "cws.security.session.timeout=60m"
})
//@formatter:on
class WebSoftwareEngineerTaskBackendApplicationTest {

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
	@DirtiesContext
	void shouldCreateRefreshToken() {
		HttpEntity<String> request = new HttpEntity<>(authJsonObject.toString(), headers);
		ResponseEntity<String> authResponse = restTemplate.postForEntity(SIGNIN_URI, request, String.class);
		DocumentContext authResponseContext = JsonPath.parse(authResponse.getBody());
		String refreshToken = authResponseContext.read("$.refreshToken");

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
	@DirtiesContext
	void shouldAuthenticateUser() {
		HttpEntity<String> request = new HttpEntity<>(authJsonObject.toString(), headers);
		ResponseEntity<String> authResponse = restTemplate.postForEntity(SIGNIN_URI, request, String.class);
		assertThat(authResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

    @Test
    @DirtiesContext
    void shouldRejectResourceAccessWithWrongAccessToken() throws Exception {
        HttpEntity<String> request = new HttpEntity<>(authJsonObject.toString(), headers);
        ResponseEntity<JwtResponse> authResponse = restTemplate.postForEntity(SIGNIN_URI, request, JwtResponse.class);
        String authToken = authResponse.getBody().getToken();
        headers.setBearerAuth("wrong" + authToken + "wrong");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("/api/users", HttpMethod.GET, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }

	@Test
	void shouldRejectUnknownUser() throws Exception {
		JSONObject unknownUserAuthObject = new JSONObject();
		unknownUserAuthObject.put("username", "unknown");
		unknownUserAuthObject.put("password", authJsonObject.get("password"));

		HttpEntity<String> authRequest = new HttpEntity<>(unknownUserAuthObject.toString(), headers);
		ResponseEntity<String> authResponse = restTemplate.postForEntity(SIGNIN_URI, authRequest, String.class);

		assertThat(authResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(authResponse.getBody()).contains("Authentication failed");
	}

	@Test
	void shouldRejectUnknownPassword() throws Exception {
		JSONObject unknownPasswordAuthObject = new JSONObject();
		unknownPasswordAuthObject.put("username", authJsonObject.get("username"));
		unknownPasswordAuthObject.put("password", "unknown");

		HttpEntity<String> authRequest = new HttpEntity<>(unknownPasswordAuthObject.toString(), headers);
		ResponseEntity<String> authResponse = restTemplate.postForEntity(SIGNIN_URI, authRequest, String.class);

		assertThat(authResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(authResponse.getBody()).contains("Authentication failed");
	}

	@Test
	@DirtiesContext
	void shouldReturnUsersPageUsesDefaultParameters() {
		HttpEntity<String> request = new HttpEntity<>(authJsonObject.toString(), headers);
		ResponseEntity<JwtResponse> authResponse = restTemplate.postForEntity(SIGNIN_URI, request, JwtResponse.class);
		String authToken = authResponse.getBody().getToken();
		headers.setBearerAuth(authToken);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange("/api/users", HttpMethod.GET, requestEntity,
				String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int usersCount = documentContext.read("$.length()");
		assertThat(usersCount).isEqualTo(5);

		JSONArray logins = documentContext.read("$..login");
		assertThat(logins).containsExactly("a-test-user-6", "c-test-user-3", "f-test-user-5", "h-test-user-8",
				"l-test-user-7");

	}

	@Test
	@DirtiesContext
	void shouldReturnDescSortedPageOfUsers() {
		HttpEntity<String> request = new HttpEntity<>(authJsonObject.toString(), headers);
		ResponseEntity<JwtResponse> authResponse = restTemplate.postForEntity(SIGNIN_URI, request, JwtResponse.class);
		String authToken = authResponse.getBody().getToken();
		headers.setBearerAuth(authToken);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange("/api/users?page=0&size=1&sort=login,desc",
				HttpMethod.GET, requestEntity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray read = documentContext.read("$[*]");
		assertThat(read).hasSize(1);

		String login = documentContext.read("$[0].login");
		assertThat(login).isEqualTo("x-test-user-4");
	}

	@Test
	@DirtiesContext
	void shouldReturnDefaultSortedPageOfUsersWithWrongPageParameterValues() {
		HttpEntity<String> request = new HttpEntity<>(authJsonObject.toString(), headers);
		ResponseEntity<JwtResponse> authResponse = restTemplate.postForEntity(SIGNIN_URI, request, JwtResponse.class);
		String authToken = authResponse.getBody().getToken();
		headers.setBearerAuth(authToken);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange("/api/users?page=-1&size=-1", HttpMethod.GET,
				requestEntity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int usersCount = documentContext.read("$.length()");
		assertThat(usersCount).isEqualTo(5);

		JSONArray logins = documentContext.read("$..login");
		assertThat(logins).containsExactly("a-test-user-6", "c-test-user-3", "f-test-user-5", "h-test-user-8",
				"l-test-user-7");
	}

	@Test
	@DirtiesContext
	void shouldReturnRequestErrorByWrongSortParameterValue() {
		HttpEntity<String> request = new HttpEntity<>(authJsonObject.toString(), headers);
		ResponseEntity<JwtResponse> authResponse = restTemplate.postForEntity(SIGNIN_URI, request, JwtResponse.class);
		String authToken = authResponse.getBody().getToken();
		headers.setBearerAuth(authToken);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange("/api/users?page=0&size=1&sort=unknown", HttpMethod.GET,
				requestEntity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

}
