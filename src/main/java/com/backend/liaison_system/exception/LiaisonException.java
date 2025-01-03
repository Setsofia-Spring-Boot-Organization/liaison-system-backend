package com.backend.liaison_system.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiaisonException extends RuntimeException {
    final Error error;

    public LiaisonException(Error error) {
        super(error.label);
        this.error = error;
    }

    public LiaisonException(Error error, Throwable cause) {
        super(error.label, cause);
        this.error = error;
    }
}
