package org.cws.web.software.engineer.task.backend.dto;


public record GithubUserDto(Long id, Long githubId, String login) {

    public GithubUserDto() {
        this(null, null, null);
    }
}
