package com.backend.liaison_system.exception;

public enum Message {

    USER_NOT_FOUND_CAUSE("The requested user does not exist"),
    THE_FOLLOWING_FIELDS_ARE_EMPTY("The following fields are empty: "),
    THE_EMAIL_OR_PASSWORD_DO_NOT_MATCH("Wrong email or password"),
    THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM("The submitted email is already in the system"),
    THE_USER_IS_NOT_AUTHORIZED("User not authorized to perform this action"),
    THE_USER_ROLE_IS_NOT_ALLOWED("The operation cannot be performed on this role"),
    THE_FOLLOWING_IDS_DO_NOT_EXIST("The following ids do not exist"),
    THE_INTERNSHIP_TYPE_IS_INCORRECT("The specified internship type is incorrect"),
    THE_FILE_SIZE_IS_BIGGER_THAN_10MB("The file size is bigger than 10MB"),
    THE_REQUESTED_ZONE_DOES_NOT_EXIST("The requested zone does not exist"),
    FAILED_TO_SUPERVISE_STUDENT("Failed to supervise student. Try again"),
    THE_EXACT_COMPANY_LOCATION_DOES_NOT_EXISTS("The exact company location cannot be found on the map");

    public final String label;
    Message(String label) {
        this.label = label;
    }
}
