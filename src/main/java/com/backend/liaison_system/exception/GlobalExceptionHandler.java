package com.backend.liaison_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LiaisonException.class)
    public ResponseEntity<ExceptionResponse> handleExceptions(LiaisonException exception, WebRequest request) {
        HttpStatus status = HttpStatus.OK; // set the default http status
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        Error error = exception.error;
        String cause = null;
        if (exception.getCause() != null) {
            cause = exception.getCause().getMessage();
        }

        switch (error) {
            case USER_NOT_FOUND -> status = HttpStatus.NOT_FOUND;
            case EMAIL_ALREADY_EXISTS -> status = HttpStatus.CONFLICT;
            case INVALID_USERNAME_OR_PASSWORD -> status= HttpStatus.UNAUTHORIZED;
            case REQUIRED_FIELDS_ARE_EMPTY,
                 INVALID_USER_IDS,
                 ERROR_SAVING_DATA,
                 INVALID_INTERNSHIP_TYPE,
                 FILE_SIZE_TOO_LARGE -> status = HttpStatus.BAD_REQUEST;
        }

        ExceptionResponse response = new ExceptionResponse(
                status.value(),
                error.label,
                cause,
                path
        );

        return new ResponseEntity<>(response, status);
    }
}
