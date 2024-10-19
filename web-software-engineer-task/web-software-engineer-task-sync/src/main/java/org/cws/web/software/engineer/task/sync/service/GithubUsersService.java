package org.cws.web.software.engineer.task.sync.service;

import java.util.List;

import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;

public interface GithubUsersService {

    List<GithubUserDTO> getUsers(Long fromId, Integer pageSize);
}
