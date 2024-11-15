package org.cws.web.software.engineer.task.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.cws.web.software.engineer.task.backend.dto.GithubUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

//@formatter:off
@DataJpaTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:cws_github",
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=password",
		"spring.jpa.defer-datasource-initialization=true",
		"spring.jpa.open-in-view=false",
		"spring.data.web.pageable.default-page-size=10"
})
@ComponentScan({ "org.cws.web.software.engineer.task.backend.service",
		"org.cws.web.software.engineer.task.backend.mapper" })
//@formatter:on
class UserServiceDefaultImplTest {

	@Autowired
	private UsersService usersService;

	@Test
	void shouldGetFirst5UsersDefaultSorting() {
		List<GithubUserDto> actualList = usersService.getUsers(PageRequest.of(0, 5));
		//@formatter:off
		assertThat(actualList)
			.hasSize(5)
			.extracting(GithubUserDto::getLogin)
			.containsExactly("a-test-user-6", "c-test-user-3", "f-test-user-5", "h-test-user-8", "l-test-user-7");
		//@formatter:on
	}

	@Test
    void shouldGetUsersWithPageSize10DescSorting() {
		List<GithubUserDto> actualList = usersService
				.getUsers(PageRequest.ofSize(10).withSort(Sort.by(Direction.DESC, "login")));
		//@formatter:off
				assertThat(actualList)
					.hasSize(10)
					.extracting(GithubUserDto::getLogin)
					.containsExactly(									
									"x-test-user-4"
									, "w-test-user-10"
									, "u-test-user-9"
									, "r-test-user-1"
									, "n-test-user-2"
									, "l-test-user-7"
									, "h-test-user-8"
									, "f-test-user-5"
									, "c-test-user-3"
									, "a-test-user-6"
									);
		//@formatter:on
	}

	@Test
    void shouldGetUsersFirstPagePageSize3DefaultSorting() {
		List<GithubUserDto> actualList = usersService.getUsers(PageRequest.ofSize(3));
		//@formatter:off
		assertThat(actualList)
			.hasSize(3)
			.extracting(GithubUserDto::getLogin)
			.containsExactly("a-test-user-6"
							, "c-test-user-3"
							, "f-test-user-5");
		//@formatter:on
	}

	@Test
    void testGet2ndPagePageSize5DefaultSorting() {
		List<GithubUserDto> actualList = usersService.getUsers(PageRequest.of(1, 5));
		//@formatter:off
		assertThat(actualList)
			.hasSize(5)
			.extracting(GithubUserDto::getLogin)
			.containsExactly("n-test-user-2"
							, "r-test-user-1"
							, "u-test-user-9"
							, "w-test-user-10"
							, "x-test-user-4");
		//@formatter:on
	}
}
