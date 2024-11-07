package org.cws.web.software.engineer.task.backend.controller;

import org.cws.web.software.engineer.task.security.exception.TokenRefreshException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RefreshTokenExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { TokenRefreshException.class })
    protected ResponseEntity<Object> handleErrorSorting(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Error sorting parameters";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
