package org.cws.web.software.engineer.task.security.exception;


public class RoleNotFoundException extends SecurityException {

    /**
     * 
     */
    private static final long serialVersionUID = -2425498798233201878L;

    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleNotFoundException(String message) {
        super(message);
    }

}
