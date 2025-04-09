package org.cws.web.software.engineer.task.security.test.service;

import org.cws.web.software.engineer.task.persistence.model.AccessToken;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.cws.web.software.engineer.task.security.service.AccessTokenService;
import org.cws.web.software.engineer.task.security.service.UserDetailsImpl;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

public class AccessTokenServiceStabImpl implements AccessTokenService {

    private JwtHandler jwtHandler;

    public AccessTokenServiceStabImpl(JwtHandler jwtHandler) {
        this.jwtHandler = jwtHandler;
    }

    @Override
    public AccessToken createAccessToken(Authentication authentication) {
        //@formatter:off
        return AccessToken.builder()
                .token(jwtHandler.generateJwtToken(authentication))
                .user(User.builder()
                        .username(((UserDetailsImpl) authentication.getPrincipal()).getUsername())
                        .build())
                .build();
        //@formatter:on
    }

    @Override
    public AccessToken createAccessToken(String username) {
        //@formatter:off
        return AccessToken.builder()
                .token(jwtHandler.generateJwtToken(username))
                .user(User.builder()
                        .username(username)
                        .build())
                .build();
        //@formatter:on
    }

    @Override
    public void revokeAccessToken(String jwtToken) {
        LoggerFactory.getLogger(this.getClass()).debug("Token revoked. Token: {}", jwtToken);
    }

    @Override
    public boolean validateAccessToken(String jwtToken) {
        return true;
    }

    @Override
    public String detectUserNameFromJwtToken(String token) {
        return jwtHandler.getUserNameFromJwtToken(token);
    }

}
