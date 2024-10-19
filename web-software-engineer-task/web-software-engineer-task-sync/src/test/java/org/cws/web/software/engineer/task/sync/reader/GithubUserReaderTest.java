package org.cws.web.software.engineer.task.sync.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Arrays;

import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.cws.web.software.engineer.task.sync.mapper.GithubUserMapper;
import org.cws.web.software.engineer.task.sync.service.GithubUsersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = { "cws.github.authorization.token=authToken" })
@AutoConfigureMockRestServiceServer
class GithubUserReaderTest {

    @Autowired
    private GithubUsersServiceImpl client;

    @Autowired
    private MockRestServiceServer  server;

    @Autowired
    private GithubUserMapper       githubUserMapper;

    @Autowired
    private ObjectMapper           objectMapper;

    private GithubUserReader       githubUserReader;

    @BeforeEach
    void init() {
        server = MockRestServiceServer.bindTo(client.getBuilder()).build();
        githubUserReader = new GithubUserReader(client, githubUserMapper, 2, 100);
    }

    @Test
    void shouldReadAllUsers() throws Exception {
        String usersPage1 = objectMapper.writeValueAsString(Arrays.asList(new GithubUserDTO("user_1", "1"), new GithubUserDTO("user_2", "2")));
        String usersPage2 = objectMapper.writeValueAsString(Arrays.asList(new GithubUserDTO("user_3", "4"), new GithubUserDTO("user_4", "4")));

        server.expect(requestTo("/api.github.com/users?since=0&per_page=2"))
                .andRespond(withSuccess(usersPage1, new MediaType("application", "vnd.github+json")));
        server.expect(requestTo("/api.github.com/users?since=2&per_page=2"))
                .andRespond(withSuccess(usersPage2, new MediaType("application", "vnd.github+json")));
        server.expect(requestTo("/api.github.com/users?since=4&per_page=2")).andRespond(withSuccess("[]", new MediaType("application", "vnd.github+json")));

        assertThat(githubUserReader.read().getLogin()).isEqualTo("user_1");
        assertThat(githubUserReader.read().getLogin()).isEqualTo("user_2");
        assertThat(githubUserReader.read().getLogin()).isEqualTo("user_3");
        assertThat(githubUserReader.read().getLogin()).isEqualTo("user_4");
        assertThat(githubUserReader.read()).isNull();

    }

    @Test
    void shouldReadOnly3Users() throws Exception {
        githubUserReader = new GithubUserReader(client, githubUserMapper, 2, 3);
        String usersPage1 = objectMapper.writeValueAsString(Arrays.asList(new GithubUserDTO("user_1", "1"), new GithubUserDTO("user_2", "2")));
        String usersPage2 = objectMapper.writeValueAsString(Arrays.asList(new GithubUserDTO("user_3", "4"), new GithubUserDTO("user_4", "4")));

        server.expect(requestTo("/api.github.com/users?since=0&per_page=2"))
                .andRespond(withSuccess(usersPage1, new MediaType("application", "vnd.github+json")));
        server.expect(requestTo("/api.github.com/users?since=2&per_page=2"))
                .andRespond(withSuccess(usersPage2, new MediaType("application", "vnd.github+json")));
        server.expect(requestTo("/api.github.com/users?since=4&per_page=2")).andRespond(withSuccess("[]", new MediaType("application", "vnd.github+json")));

        assertThat(githubUserReader.read().getLogin()).isEqualTo("user_1");
        assertThat(githubUserReader.read().getLogin()).isEqualTo("user_2");
        assertThat(githubUserReader.read().getLogin()).isEqualTo("user_3");
        assertThat(githubUserReader.read()).isNull();
    }
}
