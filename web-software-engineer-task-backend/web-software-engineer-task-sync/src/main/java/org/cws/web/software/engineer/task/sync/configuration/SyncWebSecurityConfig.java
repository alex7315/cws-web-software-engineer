package org.cws.web.software.engineer.task.sync.configuration;

import org.cws.web.software.engineer.task.security.jwt.AuthTokenFilter;
import org.cws.web.software.engineer.task.security.jwt.DelegatedAuthenticationEntryPoint;
import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
//(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
//by default
@ComponentScan({ "org.cws.web.software.engineer.task.security.jwt",
		"org.cws.web.software.engineer.task.security.service" })
public class SyncWebSecurityConfig {

	UserDetailsService userDetailsService;

    private DelegatedAuthenticationEntryPoint unauthorizedHandler;

	private JwtHandler jwtHandler;

	SyncWebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
            @Qualifier("delegatedAuthenticationEntryPoint") DelegatedAuthenticationEntryPoint unauthorizedHandler, JwtHandler jwtHandler) {
		this.userDetailsService = userDetailsService;
		this.unauthorizedHandler = unauthorizedHandler;
		this.jwtHandler = jwtHandler;
	}

	@Bean
	AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter(jwtHandler, userDetailsService);
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
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//@formatter:off
        http.csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> 
                        auth.requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs*/**").permitAll()
                        .anyRequest().authenticated()
                    );
    
        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
        //@formatter:on
	}
}
