package com.backend.liaison_system.users.lecturer.specification;

import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LecturerSpecification {

    public static Specification<Lecturer> getAllLecturers(String start, String end) {

        int startYear = Integer.parseInt(start);
        int endYear = Integer.parseInt(end);

        LocalDateTime startDate = LocalDate.of(startYear, 1, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(endYear, 12, 31).atTime(23, 59, 59, 999999999);

        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate),
                criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), endDate)
        );
    }
}
