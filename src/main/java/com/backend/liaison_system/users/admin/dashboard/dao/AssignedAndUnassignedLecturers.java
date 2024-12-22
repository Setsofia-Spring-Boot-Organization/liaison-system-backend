package com.backend.liaison_system.users.admin.dashboard.dao;

public record AssignedAndUnassignedLecturers(
        AssignedLecturers assignedLecturers,
        UnassignedLecturers unassignedLecturers,
        int totalLecturers
) { }
