package org.cws.web.software.engineer.task.backend.repository;

import org.cws.web.software.engineer.task.backend.model.GithubUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GithubUserRepository extends CrudRepository<GithubUser, Long>, PagingAndSortingRepository<GithubUser, Long> {

}
