package com.backend.liaison_system.common.requests;

public record UpdateUserDetails(
        String firstname,
        String middleName,
        String lastname,
        String phone
) { }
