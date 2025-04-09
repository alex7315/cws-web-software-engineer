package org.cws.web.software.engineer.task.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "Invalid username: Empty user name") String username,
        @NotBlank(message = "Invalid password: Empty password") String password) {}
