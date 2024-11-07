package org.cws.web.software.engineer.task.security.service;

import java.util.Optional;

import org.cws.web.software.engineer.task.persistence.model.RefreshToken;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    void deleteByUserId(Long userId);
}
