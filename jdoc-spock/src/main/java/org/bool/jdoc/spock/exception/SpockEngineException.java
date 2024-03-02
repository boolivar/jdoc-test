package org.bool.jdoc.spock.exception;

public class SpockEngineException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SpockEngineException(String message) {
        super(message);
    }

    public SpockEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
