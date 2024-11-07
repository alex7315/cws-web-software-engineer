package org.cws.web.software.engineer.task.backend.controller;

import org.cws.web.software.engineer.task.security.exception.TokenRefreshException;
import org.cws.web.software.engineer.task.security.exception.UserNotFoundException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UsersResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { PropertyReferenceException.class })
	protected ResponseEntity<Object> handleErrorSorting(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Error sorting parameters";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

    @ExceptionHandler(value = { TokenRefreshException.class })
    protected ResponseEntity<Object> handleTokenRefreshError(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Token refresh error";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = { UserNotFoundException.class })
    protected ResponseEntity<Object> handleUserNotFound(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "User found error";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
