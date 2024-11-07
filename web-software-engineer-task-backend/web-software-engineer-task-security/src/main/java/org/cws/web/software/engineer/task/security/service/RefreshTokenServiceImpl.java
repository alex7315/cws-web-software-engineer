package org.cws.web.software.engineer.task.security.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.cws.web.software.engineer.task.persistence.model.RefreshToken;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.cws.web.software.engineer.task.persistence.repository.RefreshTokenRepository;
import org.cws.web.software.engineer.task.persistence.repository.UserRepository;
import org.cws.web.software.engineer.task.security.exception.TokenRefreshException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;

    private UserRepository         userRepository;

    private int                    refreshTokenExpirationMs;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository,
            @Value("${cws.security.refresh.token.expiration.ms}") int refreshTokenExpirationMs) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {

        //@formatter:off
        User user = userRepository
                        .findById(userId)
                        .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found. User id: %d", userId)));
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationMs))
                .token(UUID.randomUUID().toString())
                .build();
        //@formatter:on
        
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Override
    public void deleteByUserId(Long userId) {
        //@formatter:off
        userRepository
            .findById(userId)
            .ifPresent(user -> refreshTokenRepository.deleteByUser(user));
        //@formatter:on
    }

}
