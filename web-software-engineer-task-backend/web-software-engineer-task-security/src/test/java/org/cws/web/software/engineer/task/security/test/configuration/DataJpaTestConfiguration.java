package org.cws.web.software.engineer.task.security.test.configuration;

import org.cws.web.software.engineer.task.persistence.repository.AccessTokenRepository;
import org.cws.web.software.engineer.task.persistence.repository.RefreshTokenRepository;
import org.cws.web.software.engineer.task.persistence.repository.RoleRepository;
import org.cws.web.software.engineer.task.persistence.repository.UserRepository;
import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.cws.web.software.engineer.task.security.service.AccessTokenService;
import org.cws.web.software.engineer.task.security.service.AccessTokenServiceImpl;
import org.cws.web.software.engineer.task.security.service.RefreshTokenService;
import org.cws.web.software.engineer.task.security.service.RefreshTokenServiceImpl;
import org.cws.web.software.engineer.task.security.service.SecurityService;
import org.cws.web.software.engineer.task.security.service.SecurityServiceImpl;
import org.cws.web.software.engineer.task.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableJpaRepositories(basePackages = { "org.cws.web.software.engineer.task.persistence.repository" })
@EntityScan("org.cws.web.software.engineer.task.persistence.model")
@ComponentScan({ "org.cws.web.software.engineer.task.security.service", "org.cws.web.software.engineer.task.security.jwt" })
public class DataJpaTestConfiguration {

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Autowired
    UserRepository        userRepository;

    @Autowired
    RoleRepository        roleRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Value("${cws.security.refresh.token.expiration.ms}")
    int                    refreshTokenExpirationMs;

    @Bean
    JwtHandler jwtHandler() {
        return new JwtHandler("Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65", 1000 * 30);
    }

    @Bean
    AccessTokenService accessTokenService(JwtHandler jwtHandler) {
        return new AccessTokenServiceImpl(accessTokenRepository, userRepository, jwtHandler);
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    SecurityService securityService(UserRepository userRepository, RoleRepository roleRepository) {
        return new SecurityServiceImpl(userRepository, roleRepository);
    }

    @Bean
    RefreshTokenService refreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        return new RefreshTokenServiceImpl(refreshTokenRepository, userRepository, refreshTokenExpirationMs);
    }
}
