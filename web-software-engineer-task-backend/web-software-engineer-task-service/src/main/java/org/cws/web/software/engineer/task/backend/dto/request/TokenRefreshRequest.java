package org.cws.web.software.engineer.task.backend.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenRefreshRequest {

    @NotBlank
    private String refreshToken;
}
