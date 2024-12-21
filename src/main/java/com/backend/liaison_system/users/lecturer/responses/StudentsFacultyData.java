package com.backend.liaison_system.users.lecturer.responses;

import java.util.List;

public record StudentsFacultyData(
        List<FacultyData> facultyData,
        int totalStudents
) {
}
