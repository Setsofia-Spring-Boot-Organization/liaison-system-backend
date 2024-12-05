package com.backend.liaison_system.users.student.response;

import java.util.List;

public record StudentDashboardRes(
        String id,
        String name,
        String email,
        String profilePictureUrl,
        boolean isSupervised,
        boolean isAssumeDuty,
        List<Colleagues> colleagues,
        int totalColleagues,
        List<AssignedLecturer> assignedLecturers,
        int totalLecturers
) { }
