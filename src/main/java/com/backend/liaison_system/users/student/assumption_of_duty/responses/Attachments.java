package com.backend.liaison_system.users.student.assumption_of_duty.responses;

import com.backend.liaison_system.enums.Status;

import java.time.LocalDateTime;

public record Attachments(
        String id,
        String name,
        String department,
        String placeOfInternship,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Status status
) {
}