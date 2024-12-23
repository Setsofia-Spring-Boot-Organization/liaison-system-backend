package com.backend.liaison_system.users.admin.dashboard.dao;

public record AssignedAndUnassignedLecturersAndStudentsData(
        AssignedAndUnassignedStudents assignedAndUnassignedStudents,
        AssignedAndUnassignedLecturers assignedAndUnassignedLecturers
) {
}
