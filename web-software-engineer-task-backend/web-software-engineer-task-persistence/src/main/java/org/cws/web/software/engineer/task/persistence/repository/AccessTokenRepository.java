package org.cws.web.software.engineer.task.persistence.repository;

import java.util.Optional;

import org.cws.web.software.engineer.task.persistence.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findByToken(String token);
}
