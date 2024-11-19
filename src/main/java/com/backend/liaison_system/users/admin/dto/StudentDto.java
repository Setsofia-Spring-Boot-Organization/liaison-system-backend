package com.backend.liaison_system.users.admin.dto;

import com.backend.liaison_system.enums.Status;
import java.time.LocalDateTime;

public record StudentDto (
        String id,
        String name,
        String department,
        String faculty,
        String age,
        String email,
        String gender,
        String phone,
        String profilePictureUrl,
        String about,
        String course,
        String placeOfInternship,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Status status
) { }
