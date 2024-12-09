package com.backend.liaison_system.users.student.response;

public record Colleagues(
        String id,
        String name,
        String email,
        String profilePictureUrl,
        String department
) { }
