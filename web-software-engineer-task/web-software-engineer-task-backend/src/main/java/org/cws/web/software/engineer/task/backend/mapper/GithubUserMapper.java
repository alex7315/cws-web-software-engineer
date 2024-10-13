package org.cws.web.software.engineer.task.backend.mapper;

import org.cws.web.software.engineer.task.backend.dto.GithubUserDto;
import org.cws.web.software.engineer.task.backend.model.GithubUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GithubUserMapper {

	@Mapping(target = "id", source = "id")
	@Mapping(target = "githubId", source = "githubId")
	@Mapping(target = "login", source = "login")
	GithubUserDto toDto(GithubUser githubUser);
}
