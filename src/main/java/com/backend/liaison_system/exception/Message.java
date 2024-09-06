package com.backend.liaison_system.exception;

public enum Message {

    USER_NOT_FOUND_CAUSE("the requested user does not exist"),
    THE_FOLLOWING_FIELDS_ARE_EMPTY("the following fields are empty: "),
    THE_EMAIL_OR_PASSWORD_DO_NOT_MATCH("wrong email or password"),
    THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM("the submitted email is already in the system");

    public final String label;
    Message(String label) {
        this.label = label;
    }
}
