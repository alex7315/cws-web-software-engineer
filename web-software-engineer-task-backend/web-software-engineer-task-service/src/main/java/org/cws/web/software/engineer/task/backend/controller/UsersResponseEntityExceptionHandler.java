package org.cws.web.software.engineer.task.backend.controller;

import java.nio.file.AccessDeniedException;

import org.cws.web.software.engineer.task.backend.dto.response.JwtResponse;
import org.cws.web.software.engineer.task.backend.dto.response.MessageResponse;
import org.cws.web.software.engineer.task.security.exception.TokenRefreshException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UsersResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(UsersResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = { AuthenticationException.class })
    protected ResponseEntity<Object> handleAuthenticationException(RuntimeException ex, WebRequest request) {
        JwtResponse response = new JwtResponse("Authentication failed");
        return handleExceptionInternal(ex, response, createHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

	@ExceptionHandler(value = { PropertyReferenceException.class })
	protected ResponseEntity<Object> handleErrorSorting(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Error sorting parameters";
        return handleExceptionInternal(ex, bodyOfResponse, createHeaders(), HttpStatus.BAD_REQUEST, request);
	}

    @ExceptionHandler(value = { TokenRefreshException.class })
    protected ResponseEntity<Object> handleTokenRefreshError(RuntimeException ex, WebRequest request) {
        JwtResponse responseBody = new JwtResponse("Token refresh error");
        return handleExceptionInternal(ex, responseBody, createHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    protected ResponseEntity<Object> accessDenied(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, new MessageResponse("Access denided"), createHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
      //@formatter:off
      String stringOfResponse = ex.getBindingResult().getAllErrors()
              .stream()
              .map(eo -> String.format("object name: %s  error message: %s%n", eo.getObjectName(), eo.getDefaultMessage()))
              .reduce("", String::concat);
      LOG.info("Handles MethodArgumentNotValidException. Response: {}", stringOfResponse);
      MessageResponse responseBody = new MessageResponse(stringOfResponse);
      //@formatter:on
        return handleExceptionInternal(ex, responseBody, createHeaders(), HttpStatus.BAD_REQUEST, request);

    }

    private HttpHeaders createHeaders() {
        return new HttpHeaders();
    }
}
