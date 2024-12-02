package org.cws.web.software.engineer.task.backend.controller;

import java.util.List;

import org.cws.web.software.engineer.task.backend.dto.request.LoginRequest;
import org.cws.web.software.engineer.task.backend.dto.request.TokenRefreshRequest;
import org.cws.web.software.engineer.task.backend.dto.response.JwtResponse;
import org.cws.web.software.engineer.task.backend.dto.response.TokenRefreshResponse;
import org.cws.web.software.engineer.task.persistence.model.AccessToken;
import org.cws.web.software.engineer.task.persistence.model.RefreshToken;
import org.cws.web.software.engineer.task.security.exception.TokenRefreshException;
import org.cws.web.software.engineer.task.security.service.AccessTokenService;
import org.cws.web.software.engineer.task.security.service.RefreshTokenService;
import org.cws.web.software.engineer.task.security.service.UserDetailsImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private AccessTokenService    accessTokenService;

	private RefreshTokenService refreshTokenService;

    public AuthController(@Autowired AuthenticationManager authenticationManager, @Autowired AccessTokenService accessTokenService,
            @Autowired RefreshTokenService refreshTokenService) {
		this.authenticationManager = authenticationManager;
        this.accessTokenService = accessTokenService;
		this.refreshTokenService = refreshTokenService;
	}

	@Operation(summary = "Authenticates user by name and password and creates web access token to request resources user is authorized for")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Authenticates user successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class)) }),
			@ApiResponse(responseCode = "401", description = "User authentication error", content = @Content) })
	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
        AccessToken accessToken = accessTokenService.createAccessToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		//@formatter:off
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();
        
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        String userName = userDetails.getUsername();
        String refreshTokenValue = refreshToken.getToken();
        LoggerFactory.getLogger(this.getClass()).debug("User name: {} refresh token: {}", userName, refreshTokenValue);
        
        return ResponseEntity.ok(JwtResponse.builder()
                                        .token(accessToken.getToken())
                                        .id(userDetails.getId())
                                        .username(userName)
                                        .email(userDetails.getEmail())
                                        .refreshToken(refreshTokenValue)
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
                                AccessToken accessToken = accessTokenService.createAccessToken(user.getUsername());
                                return ResponseEntity.ok(TokenRefreshResponse.builder()
                                                                .token(accessToken.getToken())
                                                                .refreshToken(requestRefreshToken)
                                                                .build());
                            }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
        //@formatter:on
	}

}
