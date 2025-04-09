package org.cws.web.software.engineer.task.backend.dto.response;

public record TokenRefreshResponse(String token, String type, String refreshToken, String message) {

    public TokenRefreshResponse {
        type = "Bearer";
    }
}
