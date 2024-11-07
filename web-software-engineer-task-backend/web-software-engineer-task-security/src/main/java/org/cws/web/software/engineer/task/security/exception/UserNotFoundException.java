package org.cws.web.software.engineer.task.security.exception;


public class UserNotFoundException extends SecurityException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -7041069918171684131L;

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
