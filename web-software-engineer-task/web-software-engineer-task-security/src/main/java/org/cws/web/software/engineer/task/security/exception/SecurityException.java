package org.cws.web.software.engineer.task.security.exception;


public class SecurityException extends RuntimeException {

    /**
     * 
     */
    private static final long   serialVersionUID = -6417359029896792230L;

    private static final String SECURITY         = "SECURITY: ";

    public SecurityException(String message) {
        super(SECURITY + message);
    }

    public SecurityException(String message, Throwable cause) {
        super(SECURITY + message, cause);
    }

}
