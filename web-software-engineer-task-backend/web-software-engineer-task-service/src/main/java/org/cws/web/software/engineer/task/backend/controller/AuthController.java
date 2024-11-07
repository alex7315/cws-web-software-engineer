package org.cws.web.software.engineer.task.backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cws.web.software.engineer.task.backend.dto.request.LoginRequest;
import org.cws.web.software.engineer.task.backend.dto.request.SignupRequest;
import org.cws.web.software.engineer.task.backend.dto.request.TokenRefreshRequest;
import org.cws.web.software.engineer.task.backend.dto.response.JwtResponse;
import org.cws.web.software.engineer.task.backend.dto.response.MessageResponse;
import org.cws.web.software.engineer.task.backend.dto.response.TokenRefreshResponse;
import org.cws.web.software.engineer.task.persistence.model.RefreshToken;
import org.cws.web.software.engineer.task.persistence.model.Role;
import org.cws.web.software.engineer.task.persistence.model.RoleEnum;
import org.cws.web.software.engineer.task.persistence.model.User;
import org.cws.web.software.engineer.task.security.exception.TokenRefreshException;
import org.cws.web.software.engineer.task.security.jwt.JwtHandler;
import org.cws.web.software.engineer.task.security.service.RefreshTokenService;
import org.cws.web.software.engineer.task.security.service.SecurityService;
import org.cws.web.software.engineer.task.security.service.UserDetailsImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private PasswordEncoder       passwordEncoder;

    private JwtHandler            jwtHandler;

    private SecurityService       securityService;

    private RefreshTokenService   refreshTokenService;

    public AuthController(@Autowired AuthenticationManager authenticationManager, @Autowired PasswordEncoder passwordEncoder,
            @Autowired JwtHandler jwtHandler, @Autowired SecurityService securityService, @Autowired RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtHandler = jwtHandler;
        this.securityService = securityService;
        this.refreshTokenService = refreshTokenService;
    }

    @Operation(summary = "Registers new user with user name, email and authorization list (role names)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New user is created and standard message is in response", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "401", description = "Error to register new user", content = @Content) })
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Create new user's account
        //@formatter:off
        User user = User.builder()
                        .username(signUpRequest.getUsername())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .email(signUpRequest.getEmail())
                        .build();
        //@formatter:on

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        
        if(CollectionUtils.isEmpty(strRoles)) {
            securityService.addRole(roles, RoleEnum.ROLE_USER);
        } else {
            strRoles.forEach(role -> fillRole(role, roles));
        }
        
        user.setRoles(roles);
        securityService.saveUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }

    private void fillRole(String role, Set<Role> roles) {
        if ("admin".equals(role)) {
            securityService.addRole(roles, RoleEnum.ROLE_ADMIN);
        } else {
            securityService.addRole(roles, RoleEnum.ROLE_USER);
        }
    }

    @Operation(summary = "Authenticates user by name and password and creates web access token to request resources user is authorized for")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticates user successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "User authentication error", content = @Content) })
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtHandler.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        //@formatter:off
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();
        
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        LoggerFactory.getLogger(this.getClass()).debug(String.format("User name: %s refresh token: %s", userDetails.getUsername(), refreshToken.getToken()));
            
        return ResponseEntity.ok(JwtResponse.builder()
                                        .token(jwt)
                                        .id(userDetails.getId())
                                        .username(userDetails.getUsername())
                                        .email(userDetails.getEmail())
                                        .refreshToken(refreshToken.getToken())
                                        .roles(roles)
                                        .build());
        //@formatter:on
    }

    //@formatter:off
    @Operation(summary = "Refresh expired access token using intern refresh token associated with selected user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token was refreshed successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRefreshResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Refresh token is not registered or it is expired", content = @Content)})
    //@formatter:on
    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        //@formatter:off
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                                String token = jwtHandler.generateJwtToken(user.getUsername());
                                return ResponseEntity.ok(TokenRefreshResponse.builder()
                                                                .token(token)
                                                                .refreshToken(requestRefreshToken)
                                                                .build());
                            }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
        //@formatter:on
    }

}
