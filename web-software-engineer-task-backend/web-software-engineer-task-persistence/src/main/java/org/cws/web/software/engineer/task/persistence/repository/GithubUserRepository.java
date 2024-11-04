package org.cws.web.software.engineer.task.persistence.repository;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubUserRepository extends JpaRepository<GithubUser, Long> {

    GithubUser getByGithubId(Long githubId);
}
