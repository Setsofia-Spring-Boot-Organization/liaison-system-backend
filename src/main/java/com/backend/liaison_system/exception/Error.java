package com.backend.liaison_system.exception;

public enum Error {

    USER_NOT_FOUND("user not found"),
    ERROR_SAVING_DATA("the data cannot be saved"),
    EMAIL_ALREADY_EXISTS("email already exist"),
    INVALID_USERNAME_OR_PASSWORD("login failed"),
    REQUIRED_FIELDS_ARE_EMPTY("required fields are empty");

    public final String label;
    Error(String label) {
        this.label = label;
    }
}
