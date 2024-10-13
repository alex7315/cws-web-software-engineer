package org.cws.web.software.engineer.task.backend.service;

import java.util.List;

import org.cws.web.software.engineer.task.backend.dto.GithubUserDto;
import org.springframework.data.domain.Pageable;

public interface UsersService {
	/**
	 * Gets page of whole user list according to page number, page size and sorting
	 * are set as parameter
	 * 
	 * @param pageable parameter to set number of page, page size and sorting.
	 *                 Default sorting is done by {@code login} with {@code ASC}
	 *                 order
	 * @return all {@link GithubUserDto} items paginated and sorted by the given
	 *         options
	 */
	List<GithubUserDto> getUsers(Pageable pagable);
}
