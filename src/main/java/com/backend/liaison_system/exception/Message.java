package com.backend.liaison_system.exception;

public enum Message {

    USER_NOT_FOUND_CAUSE("the requested user does not exist"),
    THE_FOLLOWING_FIELDS_ARE_EMPTY("the following fields are empty: "),
    THE_EMAIL_OR_PASSWORD_DO_NOT_MATCH("wrong email or password"),
    THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM("the submitted email is already in the system"),
    THE_USER_IS_NOT_AUTHORIZED("user not authorized to perform this action"),
    THE_USER_ROLE_IS_NOT_ALLOWED("the operation cannot be performed on this role"),
    THE_FOLLOWING_IDS_DO_NOT_EXIST("the following ids do not exist");

    public final String label;
    Message(String label) {
        this.label = label;
    }
}
