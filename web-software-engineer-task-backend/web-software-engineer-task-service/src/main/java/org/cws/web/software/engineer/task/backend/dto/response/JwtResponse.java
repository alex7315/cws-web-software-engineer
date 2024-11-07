package org.cws.web.software.engineer.task.backend.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String       token;
    @Builder.Default
    private String       type = "Bearer";
    private String       refreshToken;
    private Long         id;
    private String       username;
    private String       email;
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    private String       message;

}
