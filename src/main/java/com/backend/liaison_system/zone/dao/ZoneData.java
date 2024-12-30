package com.backend.liaison_system.zone.dao;

import java.util.Set;

public record ZoneData(
        String name,
        String region,
        Set<String> towns,
        String zoneLead,
        String startOfAcademicYear,
        String endOfAcademicYear,
        int semester,
        LecturerData lecturers
) { }
