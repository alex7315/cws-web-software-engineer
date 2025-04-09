package org.cws.web.software.engineer.task.backend.dto.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record SignupRequest(@NotBlank @Size(min = 3, max = 20) String username, @NotBlank @Size(max = 50) @Email String email, Set<String> role,
        @NotBlank @Size(min = 6, max = 40) String password) {

    public SignupRequest {
        role = Set.copyOf(role);
    }
}
