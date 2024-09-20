package com.backend.liaison_system.users.admin.dto;

import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class StudentDto {
    private String id;
    private String name;
    private String department;
    private String faculty;
    private String age;
    private String email;
    private String gender;
    private String phone;
    private String about;
    private String course;
    private String placeOfInternship;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private InternshipType type;
    private Status status;
}
