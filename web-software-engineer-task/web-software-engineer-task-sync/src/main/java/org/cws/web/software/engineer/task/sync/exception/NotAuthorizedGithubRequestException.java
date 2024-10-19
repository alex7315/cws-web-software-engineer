package org.cws.web.software.engineer.task.sync.exception;


public class NotAuthorizedGithubRequestException extends GithubSynchJobException {

    /**
     * 
     */
    private static final long serialVersionUID = -1498749459489154624L;

    public NotAuthorizedGithubRequestException(String message) {
        super(message);
    }

    public NotAuthorizedGithubRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
