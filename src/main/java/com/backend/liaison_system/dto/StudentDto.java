package com.backend.liaison_system.dto;

import com.backend.liaison_system.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class StudentDto {
    private String id;
    private String studentFirstName;
    private String studentLastName;
    private String studentOtherName;
    private String studentDepartment;
    private String studentFaculty;
    private String studentAge;
    private String studentEmail;
    private String studentGender;
    private String studentPhone;
    private String studentAbout;
    private String studentCourse;
    private String placeOfInternship;
    private String startDate;
    private String endDate;
    private Status status;
}
