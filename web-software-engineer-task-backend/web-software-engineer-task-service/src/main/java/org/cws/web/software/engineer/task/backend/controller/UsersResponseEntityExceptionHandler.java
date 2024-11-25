package org.cws.web.software.engineer.task.backend.controller;

import java.nio.file.AccessDeniedException;

import org.cws.web.software.engineer.task.backend.dto.response.JwtResponse;
import org.cws.web.software.engineer.task.security.exception.TokenRefreshException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UsersResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { AuthenticationException.class })
    protected ResponseEntity<Object> handleAuthenticationException(RuntimeException ex, WebRequest request) {
        JwtResponse response = JwtResponse.builder().message("Authentication failed").build();
        return handleExceptionInternal(ex, response, createHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

	@ExceptionHandler(value = { PropertyReferenceException.class })
	protected ResponseEntity<Object> handleErrorSorting(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Error sorting parameters";
        return handleExceptionInternal(ex, bodyOfResponse, createHeaders(), HttpStatus.BAD_REQUEST, request);
	}

    @ExceptionHandler(value = { TokenRefreshException.class })
    protected ResponseEntity<Object> handleTokenRefreshError(RuntimeException ex, WebRequest request) {
        JwtResponse responseBody = JwtResponse.builder().message("Token refresh error").build();
        return handleExceptionInternal(ex, responseBody, createHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    protected ResponseEntity<Object> accessDenied(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Access denided";
        return handleExceptionInternal(ex, bodyOfResponse, createHeaders(), HttpStatus.NOT_FOUND, request);
    }


    private HttpHeaders createHeaders() {
        return new HttpHeaders();
    }
}
