package org.cws.web.software.engineer.task.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

//@formatter:off
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"spring.datasource.url=jdbc:h2:mem:cws_github",
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=password",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.jpa.defer-datasource-initialization=true",
		"spring.data.web.pageable.default-page-size=5"
})
//@formatter:on
class WebSoftwareEngineerTaskBackendApplicationTest {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnUsersPageUsesDefaultParameters() throws Exception {
		ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int usersCount = documentContext.read("$.length()");
		assertThat(usersCount).isEqualTo(5);

		JSONArray logins = documentContext.read("$..login");
		assertThat(logins).containsExactly("a-test-user-6", "c-test-user-3", "f-test-user-5", "h-test-user-8",
				"l-test-user-7");

	}

	@Test
	void shouldReturnASortedPageOfUsers() throws Exception {
		ResponseEntity<String> response = restTemplate.getForEntity("/users?page=0&size=1&sort=login,desc",
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray read = documentContext.read("$[*]");
		assertThat(read.size()).isEqualTo(1);

		String login = documentContext.read("$[0].login");
		assertThat(login).isEqualTo("x-test-user-4");
	}

	@Test
	void shouldReturnASortedPageOfUsersWithWrongPageParameterValues() throws Exception {
		ResponseEntity<String> response = restTemplate.getForEntity("/users?page=-1&size=-1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int usersCount = documentContext.read("$.length()");
		assertThat(usersCount).isEqualTo(5);

		JSONArray logins = documentContext.read("$..login");
		assertThat(logins).containsExactly("a-test-user-6", "c-test-user-3", "f-test-user-5", "h-test-user-8",
				"l-test-user-7");
	}

	@Test
	void shouldReturnASortedPageOfUsersWithWrongSortParameterValues() throws Exception {
		ResponseEntity<String> response = restTemplate.getForEntity("/users?page=0&size=1&sort=unknown", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

	}

}
