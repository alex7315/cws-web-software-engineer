package org.cws.web.software.engineer.task.sync.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.cws.web.software.engineer.task.persistence.repository.GithubUserRepository;
import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class GithubUserProcessorTest {

	@Mock
	private GithubUserRepository githubUserRepository;

	private GithubUserProcessor githubUserProcessor;

	@BeforeEach
	void init() {
		githubUserProcessor = new GithubUserProcessor(githubUserRepository, 1L);
	}

	@Test
	void shouldFindAndModifyExistedUser() throws Exception {
		GithubUserDTO userDto = new GithubUserDTO("user_1", "1");
		//@formatter:off
		when(githubUserRepository.getByGithubId(1L))
				.thenReturn(GithubUser.builder()
									.id(100L)
									.githubId(Long.valueOf(userDto.getId()))
									.login(userDto.getLogin())
									.modificationId(0L)
									.build());
		
		GithubUser expectedUser = GithubUser.builder()
				.id(100L)
				.githubId(Long.valueOf(userDto.getId()))
				.login(userDto.getLogin())
				.modificationId(1L)
				.build();
		//@formatter:on

		GithubUser actualUser = githubUserProcessor.process(userDto);
		assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
	}

	@Test
	void shouldCreateNewUser() throws Exception {
		GithubUserDTO userDto = new GithubUserDTO("user_2", "2");
		//@formatter:off
		when(githubUserRepository.getByGithubId(1L))
				.thenReturn(null);
		
		GithubUser expectedUser = GithubUser.builder()
				.githubId(Long.valueOf(userDto.getId()))
				.login(userDto.getLogin())
				.modificationId(1L)
				.build();
		//@formatter:on

		GithubUser actualUser = githubUserProcessor.process(userDto);
		assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
	}
}
