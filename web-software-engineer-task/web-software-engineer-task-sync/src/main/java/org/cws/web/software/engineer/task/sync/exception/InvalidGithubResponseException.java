package org.cws.web.software.engineer.task.sync.exception;


public class InvalidGithubResponseException extends GithubSynchJobException {

    /**
     * 
     */
    private static final long serialVersionUID = -19561775424277291L;

    public InvalidGithubResponseException(String message) {
        super(message);
    }

    public InvalidGithubResponseException(String message, Throwable cause) {
        super(message, cause);
    }

}
