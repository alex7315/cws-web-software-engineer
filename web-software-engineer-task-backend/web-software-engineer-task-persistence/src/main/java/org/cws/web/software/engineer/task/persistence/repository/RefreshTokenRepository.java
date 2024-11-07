package org.cws.web.software.engineer.task.persistence.repository;

import java.util.Optional;

import org.cws.web.software.engineer.task.persistence.model.RefreshToken;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);
}
