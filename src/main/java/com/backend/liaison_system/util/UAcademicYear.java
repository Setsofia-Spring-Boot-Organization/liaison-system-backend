package com.backend.liaison_system.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class UAcademicYear {

    public static LocalDateTime startOfAcademicYear(String year) {
        int startYear = Integer.parseInt(year);
        LocalDate startOfYear = LocalDate.of(startYear, 1, 1);
        return startOfYear.atStartOfDay(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static LocalDateTime endOfAcademicYear(String year) {
        int endYear = Integer.parseInt(year);
        LocalDate endOfYear = LocalDate.of(endYear, 12, 31);
        LocalDateTime endOfYearTime = endOfYear.atTime(23, 59, 59, 999999999);
        ZonedDateTime zonedEndOfYearTime = endOfYearTime.atZone(ZoneId.of("UTC"));
        return zonedEndOfYearTime.toLocalDateTime();
    }
}
