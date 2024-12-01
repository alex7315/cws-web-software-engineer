package org.cws.web.software.engineer.task.security.service;

import org.cws.web.software.engineer.task.persistence.model.AccessToken;
import org.springframework.security.core.Authentication;

public interface AccessTokenService {

    AccessToken createAccessToken(Authentication authentication);

    void revokeAccessToken(String jwtToken);

    boolean validateAccessToken(String jwtToken);
}
