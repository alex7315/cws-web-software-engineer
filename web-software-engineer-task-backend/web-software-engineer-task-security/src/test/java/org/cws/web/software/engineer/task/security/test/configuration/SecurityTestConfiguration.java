package org.cws.web.software.engineer.task.security.test.configuration;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.cws.web.software.engineer.task.security.jwt.AuthTokenFilter;
import org.cws.web.software.engineer.task.security.jwt.DelegatedAuthenticationEntryPoint;
import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.cws.web.software.engineer.task.security.test.controller.SecurityTestExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@Configuration
public class SecurityTestConfiguration {

    @Value("${cws.security.jwt.secret}")
    String                           jwtSecret;

    @Value("${cws.security.jwt.expiration.ms}")
    int                              jwtExpirationMs;

    @Bean
    SecurityTestExceptionHandler securityTestExceptionHandler() {
        return new SecurityTestExceptionHandler();
    }

    @Bean
    HandlerExceptionResolver resolver() {
        return new ExceptionHandlerExceptionResolver();
    }

    @Bean("delegatedAuthenticationEntryPoint")
    AuthenticationEntryPoint unauthorizedHandler() {
        return new DelegatedAuthenticationEntryPoint(resolver());
    }

    @Bean
    JwtHandler jwtHandler() {
        return new JwtHandler(jwtSecret, jwtExpirationMs);
    }


    @Bean
    @SuppressWarnings("deprecation")
    PasswordEncoder passwordEncoder() {
        //        return NoOpPasswordEncoder.getInstance();
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("password").roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtHandler(), userDetailsService());
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService());

        return authProvider;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //@formatter:off
        http.csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler()))
            .authorizeHttpRequests(auth -> 
                        auth.anyRequest().authenticated()
                    )
           .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
           .authenticationProvider(authenticationProvider())
           .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(STATELESS));
    
        return http.build();
        //@formatter:on
    }
}
