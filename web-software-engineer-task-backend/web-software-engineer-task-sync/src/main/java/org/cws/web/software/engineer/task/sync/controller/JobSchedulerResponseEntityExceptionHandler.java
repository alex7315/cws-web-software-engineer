package org.cws.web.software.engineer.task.sync.controller;

import java.nio.file.AccessDeniedException;

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
public class JobSchedulerResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { AuthenticationException.class })
    protected ResponseEntity<Object> handleAuthenticationException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, createHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

	@ExceptionHandler(value = { PropertyReferenceException.class })
	protected ResponseEntity<Object> handleErrorSorting(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, createHeaders(), HttpStatus.BAD_REQUEST, request);
	}

    @ExceptionHandler(value = { TokenRefreshException.class })
    protected ResponseEntity<Object> handleTokenRefreshError(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, createHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    protected ResponseEntity<Object> accessDenied(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, createHeaders(), HttpStatus.NOT_FOUND, request);
    }


    private HttpHeaders createHeaders() {
        return new HttpHeaders();
    }
}
