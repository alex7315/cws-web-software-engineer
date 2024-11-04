package org.cws.web.software.engineer.task.sync.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.cws.web.software.engineer.task.sync.service.GithubUsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class GithubUserReaderTest {

	@Mock
	private GithubUsersService client;

	private GithubUserReader githubUserReader;

	@BeforeEach
	void init() {
		githubUserReader = new GithubUserReader(client, 2, 100);
	}

	@Test
	void shouldReadAllUsers() throws Exception {

		when(client.getUsers(0L, 2))
				.thenReturn(Arrays.asList(new GithubUserDTO("user_1", "1"), new GithubUserDTO("user_2", "2")));
		when(client.getUsers(2L, 2))
				.thenReturn(Arrays.asList(new GithubUserDTO("user_3", "4"), new GithubUserDTO("user_4", "4")));
		when(client.getUsers(4L, 2)).thenReturn(Collections.emptyList());

		assertThat(githubUserReader.read().getLogin()).isEqualTo("user_1");
		assertThat(githubUserReader.read().getLogin()).isEqualTo("user_2");
		assertThat(githubUserReader.read().getLogin()).isEqualTo("user_3");
		assertThat(githubUserReader.read().getLogin()).isEqualTo("user_4");
		assertThat(githubUserReader.read()).isNull();

	}

	@Test
	void shouldReadOnly3Users() throws Exception {
		githubUserReader = new GithubUserReader(client, 2, 3);

		when(client.getUsers(0L, 2))
				.thenReturn(Arrays.asList(new GithubUserDTO("user_1", "1"), new GithubUserDTO("user_2", "2")));
		when(client.getUsers(2L, 2))
				.thenReturn(Arrays.asList(new GithubUserDTO("user_3", "4"), new GithubUserDTO("user_4", "4")));
		when(client.getUsers(4L, 2)).thenReturn(Collections.emptyList());

		assertThat(githubUserReader.read().getLogin()).isEqualTo("user_1");
		assertThat(githubUserReader.read().getLogin()).isEqualTo("user_2");
		assertThat(githubUserReader.read().getLogin()).isEqualTo("user_3");
		assertThat(githubUserReader.read()).isNull();
	}
}
