package com.backend.liaison_system.users.student.response;

public record StudentDashboardRes(
        String id,
        String name,
        String email,
        String profilePictureUrl,
        boolean isSupervised,
        boolean isAssumeDuty
) { }
