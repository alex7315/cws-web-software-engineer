package org.cws.web.software.engineer.task.test.configuration;

import org.cws.web.software.engineer.task.backend.mapper.GithubUserMapper;
import org.cws.web.software.engineer.task.backend.service.UserServiceDefaultImpl;
import org.cws.web.software.engineer.task.backend.service.UsersService;
import org.cws.web.software.engineer.task.persistence.repository.GithubUserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestServiceConfiguration {

    @Autowired
    GithubUserRepository                                     githubUserRepository;

    GithubUserMapper     githubUserMapper = Mappers.getMapper(GithubUserMapper.class);

    @Value("${cws.user.service.sort.default}") 
    private String sortDefault;

    @Bean
    UsersService userService(GithubUserRepository githubUserRepository, GithubUserMapper githubUserMapper, String sortDefault) {
        return new UserServiceDefaultImpl(githubUserRepository, githubUserMapper, sortDefault);
    }
}
