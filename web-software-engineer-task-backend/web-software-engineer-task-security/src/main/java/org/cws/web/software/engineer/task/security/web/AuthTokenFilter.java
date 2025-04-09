package org.cws.web.software.engineer.task.security.web;

import java.io.IOException;

import org.cws.web.software.engineer.task.security.service.AccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class implements {@link OncePerRequestFilter} that makes a single execution per each request.<br />
 * Method {@code doFilterInternal()} parses request to get access token from request header, <br />
 * validate access token and authenticates user using valid access token. 
 */
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(AuthTokenFilter.class);

    private UserDetailsService userDetailsService;

    private AccessTokenService accessTokenService;

    public AuthTokenFilter(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, AccessTokenService accessTokenService) {
        this.userDetailsService = userDetailsService;
        this.accessTokenService = accessTokenService;
    }


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = parseJwt(request);
        if (jwt != null && accessTokenService.validateAccessToken(jwt)) {
            authenticateUserByToken(request, jwt);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUserByToken(HttpServletRequest request, String jwt) {
        try {
            String username = accessTokenService.detectUserNameFromJwtToken(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            LOG.error("Cannot set user authentication", e);
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
