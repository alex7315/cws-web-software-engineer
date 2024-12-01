package org.cws.web.software.engineer.task.backend.config;

import static org.springframework.security.config.http.SessionCreationPolicy.ALWAYS;

import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.cws.web.software.engineer.task.security.web.AuthTokenFilter;
import org.cws.web.software.engineer.task.security.web.DelegatedAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@EnableMethodSecurity
//(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
//by default
@ComponentScan({ "org.cws.web.software.engineer.task.security.jwt",
		"org.cws.web.software.engineer.task.security.service" })
public class WebSecurityConfig {

	UserDetailsService userDetailsService;

    private DelegatedAuthenticationEntryPoint unauthorizedHandler;

	private JwtHandler jwtHandler;

	WebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
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
    SecurityFilterChain filterChain(HttpSecurity http, @Qualifier("accessTokenLogoutHandler") LogoutHandler accessTokenLogoutHandler) throws Exception {
		//@formatter:off
        http.csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .authorizeHttpRequests(auth -> 
                        auth.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs*/**").permitAll()
                        /*pre-flight (OPTIONS) requests have to be not authenticated 
                         * (avoids Firefox problem with Authorization header of OPTIONS request) */
                        .requestMatchers(HttpMethod.OPTIONS).permitAll() 
                        .anyRequest().authenticated()
                    );
    
        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(ALWAYS));
        
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
