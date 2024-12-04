package com.backend.liaison_system.users.student.response;

import java.util.List;

public record StudentDashboardRes(
        String id,
        String name,
        String email,
        String profilePictureUrl,
        boolean isSupervised,
        boolean isAssumeDuty,
        List<AssignedLecturer> assignedLecturers
) { }
