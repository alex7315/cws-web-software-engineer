package org.cws.web.software.engineer.task.security.jwt;

import java.io.IOException;

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
 * Method <code>commence()</code> is triggered anytime unautheticated user requests a secured HTTP resource <br />
 * and {@link AuthenticationException} is thrown.
 * 
 */
@Component("delegatedAuthenticationEntryPoint")
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private HandlerExceptionResolver resolver;

    public DelegatedAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        resolver.resolveException(request, response, null, authException);
    }

}
