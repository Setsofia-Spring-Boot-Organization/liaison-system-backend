package com.backend.liaison_system.zone.dao;

import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.zone.entity.Towns;
import com.backend.liaison_system.zone.entity.ZoneLecturers;
import java.time.LocalDateTime;

public record AllZones(
        String id,
        String name,
        String region,
        String zoneLead,
        LocalDateTime startOfAcademicYear,
        LocalDateTime endOfAcademicYear,

        LocalDateTime dateCreated,
        LocalDateTime dateUpdated,

        InternshipType internshipType,

        ZoneLecturers lecturers,
        Towns towns,
        int totalLecturers
) { }
