package com.backend.liaison_system.exception;

public enum Cause {

    USER_NOT_FOUND_CAUSE("the requested user does not exist"),
    THE_FOLLOWING_FIELDS_ARE_EMPTY("the following fields are empty: ");

    public final String label;
    Cause(String label) {
        this.label = label;
    }
}
