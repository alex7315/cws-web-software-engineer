package org.cws.web.software.engineer.task.sync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GithubUserDTO {

    private String login;
    private String id;
}
