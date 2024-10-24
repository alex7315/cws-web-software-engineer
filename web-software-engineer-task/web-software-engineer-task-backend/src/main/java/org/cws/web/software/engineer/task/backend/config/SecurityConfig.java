package org.cws.web.software.engineer.task.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity /*(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)*/
//by default
public class SecurityConfig {
	/*
	 * @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	 * http.authorizeHttpRequests((authz) ->
	 * authz.anyRequest().authenticated()).oauth2Login(); return http.build(); }
	 */
}
