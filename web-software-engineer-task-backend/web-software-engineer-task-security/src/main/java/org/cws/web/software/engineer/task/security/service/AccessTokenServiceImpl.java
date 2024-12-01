package org.cws.web.software.engineer.task.security.service;

import java.util.Optional;

import org.cws.web.software.engineer.task.persistence.model.AccessToken;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.cws.web.software.engineer.task.persistence.repository.AccessTokenRepository;
import org.cws.web.software.engineer.task.persistence.repository.UserRepository;
import org.cws.web.software.engineer.task.security.exception.UserNotFoundException;
import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    private AccessTokenRepository accessTokenRepository;
    private UserRepository        userRepository;
    private JwtHandler            jwtHandler;

    public AccessTokenServiceImpl(AccessTokenRepository accessTokenRepository, UserRepository userRepository, JwtHandler jwtHandler) {
        this.accessTokenRepository = accessTokenRepository;
        this.userRepository = userRepository;
        this.jwtHandler = jwtHandler;
    }

    @Override
    @Transactional(readOnly = false)
    public AccessToken createAccessToken(Authentication authentication) {
        String jwtToken = jwtHandler.generateJwtToken(authentication);
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new UserNotFoundException(String.format("User is not found. User name: %s", userPrincipal.getUsername())));
        AccessToken accessToken = AccessToken.builder().token(jwtToken).user(user).build();
        return accessTokenRepository.save(accessToken);
    }

    @Override
    @Transactional(readOnly = false)
    public void revokeAccessToken(String jwtToken) {
        Optional<AccessToken> accessTokenOptional = accessTokenRepository.findByToken(jwtToken);
        if (accessTokenOptional.isPresent()) {
            AccessToken accessToken = accessTokenOptional.get();
            accessTokenRepository.delete(accessToken);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public boolean validateAccessToken(String jwtToken) {
        Optional<AccessToken> accessTokenOptional = accessTokenRepository.findByToken(jwtToken);
        if (accessTokenOptional.isPresent()) {
            return validationAction(accessTokenOptional.get());
        }
        return false;
    }

    private boolean validationAction(AccessToken accessToken) {
        boolean validated = jwtHandler.validateJwtToken(accessToken.getToken());
        if (!validated) {
            accessTokenRepository.delete(accessToken);
        }
        return validated;
    }

}
