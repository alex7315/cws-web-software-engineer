package org.cws.web.software.engineer.task.sync.mapper;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GithubUserMapper {

    @Mapping(target = "githubId", source = "id")
    @Mapping(target = "login", source = "login")
    @Mapping(target = "modifiedAt", ignore = true)
    GithubUser toModel(GithubUserDTO dto);
}
