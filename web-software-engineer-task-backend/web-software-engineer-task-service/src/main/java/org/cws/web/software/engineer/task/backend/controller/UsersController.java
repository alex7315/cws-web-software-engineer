package org.cws.web.software.engineer.task.backend.controller;

import java.util.List;

import org.cws.web.software.engineer.task.backend.dto.GithubUserDto;
import org.cws.web.software.engineer.task.backend.service.UsersService;
import org.cws.web.software.engineer.task.security.authority.Authority;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService userService;

	public UsersController(UsersService userService) {
		this.userService = userService;
	}

	@Operation(summary = "Get paginated list of github users")
	//@formatter:off
	@Parameters(value = {
			@Parameter(name = "page", in = ParameterIn.QUERY, description = "number of page, default value 0"), 
			@Parameter(name = "size", in = ParameterIn.QUERY, description = "size of page, default value 20 or can be set by property spring.data.web.pageable.default-page-size"),
			@Parameter(name = "sort", in = ParameterIn.QUERY, description = "sort attribute and direction", example = "sort=login,asc")
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found list of github users",
					content = {@Content(mediaType = "application/json"
					, schema = @Schema(implementation = GithubUserDto.class))}),
			@ApiResponse(responseCode = "400", description = "Bad pagination parameters, e.g. unknown sorting attribute",
							content = @Content),
	})
	//@formatter:on
    @PreAuthorize(Authority.USER_OR_ADMIN)
	@GetMapping
	public ResponseEntity<List<GithubUserDto>> getAll(Pageable pageable) {
		return ResponseEntity.ok(userService.getUsers(pageable));
	}

}
