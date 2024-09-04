package com.backend.liaison_system.dto;

import com.backend.liaison_system.enums.UserRoles;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewUserRequest {
    private String firstName;
    private String lastName;
    private String otherName;
    private String email;
    private String password;
    private UserRoles role;
}
