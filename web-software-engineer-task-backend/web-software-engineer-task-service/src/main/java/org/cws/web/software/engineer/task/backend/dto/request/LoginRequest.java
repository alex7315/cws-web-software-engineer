package org.cws.web.software.engineer.task.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Invalid username: Empty user name")
    private String username;

    @NotBlank(message = "Invalid password: Empty password")
    private String password;

}
