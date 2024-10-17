package org.cws.web.software.engineer.task.backend.service;

import java.util.List;

import org.cws.web.software.engineer.task.backend.dto.GithubUserDto;
import org.cws.web.software.engineer.task.backend.mapper.GithubUserMapper;
import org.cws.web.software.engineer.task.backend.repository.GithubUserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceDefaultImpl implements UsersService {

	private GithubUserRepository githubUserRepository;

	private GithubUserMapper githubUserMapper;

	public UserServiceDefaultImpl(GithubUserRepository githubUserRepository, GithubUserMapper githubUserMapper) {
		this.githubUserRepository = githubUserRepository;
		this.githubUserMapper = githubUserMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GithubUserDto> getUsers(Pageable pageable) {
		//@formatter:off
		return githubUserRepository
					.findAll(PageRequest.of(pageable.getPageNumber()
										, pageable.getPageSize()
										, pageable.getSortOr(Sort.by(Sort.Direction.ASC, "login"))))
					.map(ghu -> githubUserMapper.toDto(ghu))
					.toList();
		//@formatter:on
	}

}
