package org.cws.web.software.engineer.task.backend.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenRefreshRequest {

    private String refreshToken;
}
