package com.backend.liaison_system.users.admin.dashboard.dao;

import com.backend.liaison_system.users.admin.dto.StudentDto;

import java.util.List;

public record UnassignedStudents(
        List<StudentDto> unAssignedStudents,
        int count
) {
}
