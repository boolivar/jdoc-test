package org.bool.jdoc.core.exception;

public class JdocException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JdocException(String message) {
        super(message);
    }

    public JdocException(String message, Throwable cause) {
        super(message, cause);
    }
}
