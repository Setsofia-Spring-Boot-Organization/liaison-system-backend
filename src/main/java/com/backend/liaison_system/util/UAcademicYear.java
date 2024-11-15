package com.backend.liaison_system.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class UAcademicYear {

    public static LocalDateTime startOfAcademicYear(String year) {
        int startYear = Integer.parseInt(year);

        return LocalDate.of(startYear, 1, 1).atStartOfDay();
    }

    public static LocalDateTime endOfAcademicYear(String year) {
        int endYear = Integer.parseInt(year);

        return LocalDate.of(endYear, 12, 31).atTime(23, 59, 59, 999999999);
    }
}
