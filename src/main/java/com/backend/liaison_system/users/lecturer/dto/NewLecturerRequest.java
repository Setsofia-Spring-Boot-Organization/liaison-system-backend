package com.backend.liaison_system.users.lecturer.dto;

import com.backend.liaison_system.enums.UserRoles;

public record NewLecturerRequest (

    String firstName,
    String lastName,
    String otherName,
    String email,
    String userProfilePicture,

    String phone,
    String company,
    String password,

    String faculty,
    String department,

    UserRoles role
) { }
