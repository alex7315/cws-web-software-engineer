package org.cws.web.software.engineer.task.backend.controller;

import java.util.List;

import org.cws.web.software.engineer.task.backend.dto.GithubUserDto;
import org.cws.web.software.engineer.task.backend.service.UsersService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

	private UsersService userService;

	public UsersController(UsersService userService) {
		this.userService = userService;
	}

	@GetMapping("/users")
	public ResponseEntity<List<GithubUserDto>> getAll(Pageable pageable) {
		return ResponseEntity.ok(userService.getUsers(pageable));
	}

}
