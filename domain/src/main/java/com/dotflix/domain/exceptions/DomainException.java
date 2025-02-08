package com.dotflix.domain.exceptions;

import java.util.List;
import com.dotflix.domain.lixo.validation.Error;

public class DomainException extends RuntimeException {
    private final List<Error> errors;

    private DomainException(final String message, final List<Error> errors){
        super(message);   // disable stack trace = + performance
        this.errors = errors;
    }

    public static DomainException with(final Error error){
        return new DomainException(error.message(), List.of(error));
    }

    public static DomainException with(final List<Error> errors){
        return new DomainException("", errors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
