package com.backend.liaison_system.users.lecturer.responses;

import com.backend.liaison_system.users.admin.dto.StudentDto;

import java.util.List;

public record StudentsData(
        List<StudentDto> students,
        int totalStudents
) { }
