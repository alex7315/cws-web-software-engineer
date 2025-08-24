package org.cws.web.software.engineer.task.backend.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.cws.web.software.engineer.task.security.service.AccessTokenService;
import org.cws.web.software.engineer.task.security.web.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableMethodSecurity
//(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
//by default
@ComponentScan({ "org.cws.web.software.engineer.task.security.jwt",
        "org.cws.web.software.engineer.task.security.service", "org.cws.web.software.engineer.task.security.web" })
public class WebSecurityConfig {

    private UserDetailsService       userDetailsService;

    private AuthenticationEntryPoint unauthorizedHandler;

    private AccessTokenService       accessTokenService;

	WebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
            @Qualifier("delegatedAuthenticationEntryPoint") AuthenticationEntryPoint delegatedAuthenticationEntryPoint,
            AccessTokenService accessTokenService) {
		this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = delegatedAuthenticationEntryPoint;
        this.accessTokenService = accessTokenService;
	}

	@Bean
	AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(userDetailsService, accessTokenService);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
	}

	@Bean
    SecurityFilterChain filterChain(HttpSecurity http, @Qualifier("accessTokenLogoutHandler") LogoutHandler accessTokenLogoutHandler) throws Exception {
		//@formatter:off
        http
            //secure access token (JWT) is used as credentials
            //no session is used on the server-side
            //it is expected, that user does not persist credentials or stores credentials in the Browser Storage
            //in this case CSRF protection can be disabled
            .csrf(csrf -> csrf.disable())
            .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(STATELESS))
            //using of Content-Security-Policy (CSP) to prevent Cross-Site Scripting (XSS) Attack 
            //java script resources can be loaded only from the same origin as the document (using of "script-src 'self'" policy directive)  
            .headers(headersConfigurer -> 
                            headersConfigurer.xssProtection(xxssConfig -> 
                                                                xxssConfig.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                                             .contentSecurityPolicy(contentSecurityPolicyConfig -> 
                                                                 contentSecurityPolicyConfig.policyDirectives("script-src 'self'")))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .authorizeHttpRequests(auth -> 
                        auth.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs*/**").permitAll()
                        /*pre-flight requests (OPTIONS) have not be authenticated 
                         * (avoids Firefox problem with Authorization header of OPTIONS request) */
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
                        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
                        .anyRequest().authenticated());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        LogoutHandler compositLogoutHandler = new CompositeLogoutHandler(accessTokenLogoutHandler, new SecurityContextLogoutHandler());
        http.logout(lc -> lc
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(compositLogoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> 
                                            SecurityContextHolder.clearContext())
                );
    
        return http.build();
        //@formatter:on
	}
}
