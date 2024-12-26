package org.cws.web.software.engineer.task.test.configuration;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
public class TestSecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(STATELESS))
            //using of Content-Security-Policy (CSP) to prevent Cross-Site Scripting (XSS) Attack 
            //java script resources can be loaded only from the same origin as the document (using of "script-src 'self'" policy directive)  
            .headers(headersConfigurer -> 
                            headersConfigurer.xssProtection(xxssConfig -> 
                                                                xxssConfig.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                                             .contentSecurityPolicy(contentSecurityPolicyConfig -> contentSecurityPolicyConfig.policyDirectives("script-src 'self'")));
        //@formatter:on
        return http.build();
    }
}
