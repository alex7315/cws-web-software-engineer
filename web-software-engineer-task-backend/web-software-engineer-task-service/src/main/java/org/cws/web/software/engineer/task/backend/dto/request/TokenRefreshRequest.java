package org.cws.web.software.engineer.task.backend.dto.request;


import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(@NotBlank String refreshToken) {}
