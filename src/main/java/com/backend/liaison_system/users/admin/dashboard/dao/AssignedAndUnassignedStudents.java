package com.backend.liaison_system.users.admin.dashboard.dao;

public record AssignedAndUnassignedStudents(
        AssignedStudents assignedStudents,
        UnassignedStudents unassignedStudents,
        int totalStudents
) { }
