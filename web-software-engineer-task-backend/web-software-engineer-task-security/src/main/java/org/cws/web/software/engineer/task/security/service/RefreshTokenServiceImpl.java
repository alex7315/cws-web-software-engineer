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
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	private final UserRepository userRepository;

	private final int refreshTokenExpirationMs;

	public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository,
			@Value("${cws.security.refresh.token.expiration.ms}") int refreshTokenExpirationMs) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.userRepository = userRepository;
		this.refreshTokenExpirationMs = refreshTokenExpirationMs;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	@Transactional(readOnly = false)
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
	@Transactional(readOnly = false)
	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(),
					"Refresh token was expired. Please make a new signin request");
		}

		return token;
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteByUserId(Long userId) {
		//@formatter:off
        userRepository
            .findById(userId)
            .ifPresent(refreshTokenRepository::deleteByUser);
        //@formatter:on
	}

}
