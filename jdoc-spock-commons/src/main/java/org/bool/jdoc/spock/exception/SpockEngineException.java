package org.bool.jdoc.spock.exception;

import org.bool.jdoc.core.exception.JdocException;

public class SpockEngineException extends JdocException {

    private static final long serialVersionUID = 1L;

    public SpockEngineException(String message) {
        super(message);
    }

    public SpockEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
