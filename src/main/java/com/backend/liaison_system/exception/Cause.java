package com.backend.liaison_system.exception;

public enum Cause {

    USER_NOT_FOUND_CAUSE("the requested user does not exist");

    public final String label;
    Cause(String label) {
        this.label = label;
    }
}
