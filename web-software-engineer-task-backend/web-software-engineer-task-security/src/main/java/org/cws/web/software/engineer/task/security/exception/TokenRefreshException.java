package org.cws.web.software.engineer.task.security.exception;


public class TokenRefreshException extends SecurityException {

    /**
     * 
     */
    private static final long serialVersionUID = -3597248470383618515L;

    public TokenRefreshException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
