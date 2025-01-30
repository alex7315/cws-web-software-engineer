package org.cws.web.software.engineer.task.security.web;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class implements {@link AuthenticationEntryPoint} with method <code>commence()</code> <br /> 
 * Method <code>commence()</code> is triggered anytime unauthenticated user requests a secured HTTP resource <br />
 * and {@link AuthenticationException} is thrown. <br />
 * This implementation uses {@link HandlerExceptionResolver} to delegate handling of {@link AuthenticationException} <br />
 * to {@code @ExceptionHandler} applied to a certain Controller (e.g. by using of {@code @ControllerAdvice}) 
 */
@Component("delegatedAuthenticationEntryPoint")
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOG = LoggerFactory.getLogger(DelegatedAuthenticationEntryPoint.class);

    private HandlerExceptionResolver resolver;

    public DelegatedAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOG.error("Unauthorized error: {} ", authException.getMessage(), authException);
        resolver.resolveException(request, response, null, authException);
    }

}
