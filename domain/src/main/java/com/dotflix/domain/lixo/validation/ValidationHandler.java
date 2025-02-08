package com.dotflix.domain.lixo.validation;

import java.util.List;

/**
 * Fluent Interface
 */
public interface ValidationHandler {
    ValidationHandler append(Error error);

    ValidationHandler append(ValidationHandler handler);

    ValidationHandler validate(Validation validation);

    default boolean hasError(){
        return getErrors() != null && !getErrors().isEmpty();
    }

    List<Error> getErrors();

    public interface Validation {
        void validate();
    }
}
