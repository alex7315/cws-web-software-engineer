package org.cws.web.software.engineer.task.security.web;

import org.cws.web.software.engineer.task.security.service.AccessTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AccessTokenLogoutHandler implements LogoutHandler{

    private AccessTokenService accessTokenService;

    public AccessTokenLogoutHandler(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        var accessTokenString = detectAccessToken(request);

        if (StringUtils.hasLength(accessTokenString)) {
            accessTokenService.revokeAccessToken(accessTokenString);
        }

    }

    private String detectAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasLength(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        return null;
    }

}
