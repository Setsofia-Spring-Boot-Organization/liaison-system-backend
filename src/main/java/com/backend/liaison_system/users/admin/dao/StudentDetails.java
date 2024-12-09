package com.backend.liaison_system.users.admin.dao;

public record StudentDetails(
        String id,
        String name,
        String email,
        String phone,
        String profilePictureUrl,
        boolean isSupervised,
        boolean isAssumeDuty
) { }
