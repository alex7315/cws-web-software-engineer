package org.cws.web.software.engineer.task.sync.exception;


public class GithubSynchJobException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -7064689495455551980L;

    private static final String GITHUB_SYNCH_JOB = "GITHUB_SYNCH_JOB: ";

    public GithubSynchJobException(String message) {
        super(GITHUB_SYNCH_JOB + message);
    }

    public GithubSynchJobException(String message, Throwable cause) {
        super(GITHUB_SYNCH_JOB + message, cause);
    }

}
