package org.cws.web.software.engineer.task.persistence.repository;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GithubUserRepository extends CrudRepository<GithubUser, Long>, PagingAndSortingRepository<GithubUser, Long> {

}
