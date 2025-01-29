package org.cws.web.software.engineer.task.sync.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Arrays;
import java.util.List;

import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.cws.web.software.engineer.task.sync.exception.InvalidGithubResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockRestServiceServer
class GithubUsersServiceImplTest {

	@Autowired
	private MockRestServiceServer server;

	private GithubUsersServiceImpl client;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void init() {
        client = new GithubUsersServiceImpl("apiVersion", "authToken", "https://api.github.com/users", RestClient.builder(), new ObjectMapper());
		server = MockRestServiceServer.bindTo(client.getBuilder()).build();
	}

	@Test
	void shouldReturnUserList() throws Exception {
		String usersString = objectMapper
				.writeValueAsString(Arrays.asList(new GithubUserDTO("user_1", "1"), new GithubUserDTO("user_2", "2")));
		server.expect(requestTo("https://api.github.com/users?since=0&per_page=2"))
				.andRespond(withSuccess(usersString, new MediaType("application", "vnd.github+json")));
		List<GithubUserDTO> actualUsers = client.getUsers(0L, 2);

		assertThat(actualUsers.stream().map(GithubUserDTO::getLogin)).containsExactlyInAnyOrder("user_1", "user_2");
	}

	@Test
    void shouldThrowResponseExceptionByNotSuccessfulStatusCode() {
		this.server.expect(requestTo("https://api.github.com/users?since=0&per_page=2"))
				.andRespond(withStatus(HttpStatusCode.valueOf(304)));
		InvalidGithubResponseException exception = catchThrowableOfType(() -> client.getUsers(0L, 2),
				InvalidGithubResponseException.class);

		assertThat(exception.getMessage()).contains("since: 0", "per_page: 2", "Status code: 304");
	}
}
