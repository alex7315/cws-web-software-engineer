package org.cws.web.software.engineer.task.security.jwt;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.cws.web.software.engineer.task.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JwtHandler.class);

    private String jwtSecret;

    private int    jwtExpirationMs;

    public JwtHandler(@Value("${cws.security.jwt.secret}") String jwtSecret, @Value("${cws.security.jwt.expiration.ms}") int jwtExpirationMs) {
        super();
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return generateJwtToken(userPrincipal.getUsername());
    }

    public String generateJwtToken(String userName) {
        //@formatter:off
        return Jwts.builder()
                    .subject(userName)
                    .issuedAt(Date.from(Instant.now()))
                    .expiration(Date.from(Instant.now().plusMillis(jwtExpirationMs)))
                    .signWith(generateKey()) //since secret key was generated, MAC algorithm will be used
                    .compact();
        //@formatter:on
    }

    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        //@formatter:off
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        //@formatter:on
    }

    public boolean validateJwtToken(String authToken) {
        if (authToken == null) {
            LOG.error("JWT is null");
            return false;
        }

        try {
            Jwts.parser().verifyWith(generateKey()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            LOG.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOG.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOG.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOG.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
