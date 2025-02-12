package com.backend.liaison_system.util;

import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

    public static String sanitizeLocalDateTimeToAcademicYear(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return formatter.format(dateTime);
    }

    public static LocalDateTime stringDateToLocalDateTime(String date) {
        return LocalDateTime.parse(date + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
