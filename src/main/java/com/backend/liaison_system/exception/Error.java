package com.backend.liaison_system.exception;

public enum Error {

    USER_NOT_FOUND("user not found");

    public final String label;
    Error(String label) {
        this.label = label;
    }
}
