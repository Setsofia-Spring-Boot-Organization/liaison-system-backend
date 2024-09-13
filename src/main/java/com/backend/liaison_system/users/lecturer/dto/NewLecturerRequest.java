package com.backend.liaison_system.users.lecturer.dto;

import com.backend.liaison_system.enums.UserRoles;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewLecturerRequest {

    private String firstName;
    private String lastName;
    private String otherName;
    private String email;
    private String dp;

    private String phone;
    private String company;
    private String password;

    private String faculty;
    private String department;

    private UserRoles role;
}
