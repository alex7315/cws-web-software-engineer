package org.cws.web.software.engineer.task.backend.dto.response;

import java.util.Collections;
import java.util.List;

public record JwtResponse(String token, String type, String refreshToken, Long id, String username, String email, List<String> roles,
        String message) {

    public JwtResponse {
        type = "Bearer";
        roles = List.copyOf(roles);
    }

    public JwtResponse(String message) {
        this("", "", "", null, "", "", Collections.emptyList(), message);
    }
}
