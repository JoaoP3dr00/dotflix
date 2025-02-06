package com.dotflix.domain.exceptions;

/**
 * Exception class for exceptions without stack trace, impactiong on a better performance,
 * furthermore, we will display error messages, so we dont need stack trace
 */
public class NoStackTraceException extends RuntimeException {
    public NoStackTraceException(final String message){
        this(message, null);
    }

    public NoStackTraceException(final String message, final Throwable cause){
        super(message, cause, true, false);
    }
}
