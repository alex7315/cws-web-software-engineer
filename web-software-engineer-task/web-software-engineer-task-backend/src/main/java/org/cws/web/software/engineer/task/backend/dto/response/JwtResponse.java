package org.cws.web.software.engineer.task.backend.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {

    private String       token;
    @Builder.Default
    private String       type = "Bearer";
    private Long         id;
    private String       username;
    private String       email;
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    private String       message;

}
