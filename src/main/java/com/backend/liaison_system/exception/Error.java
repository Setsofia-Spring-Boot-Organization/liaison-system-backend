package com.backend.liaison_system.exception;

public enum Error {

    USER_NOT_FOUND("user not found"),
    ERROR_SAVING_DATA("the data cannot be saved"),
    EMAIL_ALREADY_EXISTS("email already exist"),
    INVALID_USERNAME_OR_PASSWORD("login failed"),
    REQUIRED_FIELDS_ARE_EMPTY("required fields are empty"),
    UNAUTHORIZED_USER("authorization failed"),
    WRONG_USER_ROLE("wrong user role"),
    INVALID_USER_IDS("invalid user ids"),
    INVALID_INTERNSHIP_TYPE("invalid internship type"),
    FILE_SIZE_TOO_LARGE("file size too large");

    public final String label;
    Error(String label) {
        this.label = label;
    }
}
