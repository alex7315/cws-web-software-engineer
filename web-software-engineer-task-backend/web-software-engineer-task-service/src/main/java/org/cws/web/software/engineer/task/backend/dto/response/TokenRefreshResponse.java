package org.cws.web.software.engineer.task.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRefreshResponse {

    private String token;
    @Builder.Default
    private String type = "Bearer";
    private String refreshToken;
    private String message;
}
