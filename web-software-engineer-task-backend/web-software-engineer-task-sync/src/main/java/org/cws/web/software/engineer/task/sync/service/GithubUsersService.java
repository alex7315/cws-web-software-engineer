package org.cws.web.software.engineer.task.sync.service;

import java.util.List;

import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;

/**
 * Service provides functionality is needed to sync github users
 */
public interface GithubUsersService {

	/**
	 * gets list of github users started from {@code fromId} and sized by
	 * {@code pageSize}
	 * 
	 * @param fromId   start user id to get list of github users
	 * @param pageSize size of githb user list
	 * @return list of github users. User represents by {@link GithubUserDTO}
	 */
	List<GithubUserDTO> getUsers(Long fromId, Integer pageSize);
}
