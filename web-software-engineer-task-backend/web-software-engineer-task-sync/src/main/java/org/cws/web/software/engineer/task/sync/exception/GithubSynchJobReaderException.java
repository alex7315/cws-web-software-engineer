package org.cws.web.software.engineer.task.sync.exception;


public class GithubSynchJobReaderException extends GithubSynchJobException {

    /**
     * 
     */
    private static final long serialVersionUID = 5166680965266443422L;

    public GithubSynchJobReaderException(String message) {
        super(message);
    }

    public GithubSynchJobReaderException(String message, Throwable cause) {
        super(message, cause);
    }

}
