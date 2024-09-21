package com.backend.liaison_system.util;

import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.users.student.Student;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This is a class that contains various specifications for filtering the Student table
 */
public class StudentSpecifications {
    public static Specification<Student> hasName(String name) {
        return ((root, query, criteriaBuilder) -> name == null || name.isEmpty() ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("studentFirstName")), "%" + name.toLowerCase() + "%"));
    }

    public static Specification<Student> hasLastName(String name) {
        return ((root, query, criteriaBuilder) -> name == null || name.isEmpty() ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("studentLastName")), "%" + name.toLowerCase() + "%"));
    }

    public static Specification<Student> hasOtherName(String name) {
        return ((root, query, criteriaBuilder) -> name == null || name.isEmpty() ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("studentOtherName")), "%" + name.toLowerCase() + "%"));
    }

    public static Specification<Student> sameFaculty(String faculty) {
        return ((root, query, criteriaBuilder) -> faculty == null || faculty.isEmpty() ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("studentFaculty")), "%" + faculty.toLowerCase() + "%"));
    }

    public static Specification<Student> sameDepartment(String department) {
        return ((root, query, criteriaBuilder) -> department == null || department.isEmpty() ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("studentDepartment")), "%" + department.toLowerCase() + "%"));
    }

    public static Specification<Student> internshipType(InternshipType internshipType) {
        return ((root, query, criteriaBuilder) -> internshipType == null  ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("internshipType")), "%" + internshipType + "%"));
    }

    public static Specification<Student> academicYearAndInternshipType(String start, String end, String type) {
        // format the start and end year into a LocalDateTime object
        int startYear = Integer.parseInt(start);
        int endYear = Integer.parseInt(end);

        LocalDateTime startDate = LocalDate.of(startYear, 1, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(endYear, 12, 31).atTime(23, 59, 59, 999999999);

        return (((root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate),
                criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate),
                criteriaBuilder.equal(root.get("internshipType"), type))
        ));
    }
}
