package org.bool.jdoc.spock.exception;

import java.io.Serial;

public class SpockEngineException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SpockEngineException(String message) {
        super(message);
    }

    public SpockEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
