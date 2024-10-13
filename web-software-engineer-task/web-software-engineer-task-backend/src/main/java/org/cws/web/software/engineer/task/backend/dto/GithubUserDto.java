package org.cws.web.software.engineer.task.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GithubUserDto {
	private Long id;
	
	private Long githubId;
	
	private String login;
}
