package com.backend.liaison_system.util;

import com.backend.liaison_system.common.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.users.student.Student;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * This is a class that contains various specifications for filtering the Student table
 */
public class StudentSpecifications {

    public static Specification<Student> academicYearAndInternshipType(ConstantRequestParam param) {
        // format the start and end year into a LocalDateTime object

        LocalDateTime startYear = UAcademicYear.startOfAcademicYear(param.startOfAcademicYear());
        LocalDateTime endYear = UAcademicYear.endOfAcademicYear(param.endOfAcademicYear());

        return (((root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startYear),
                criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endYear),
                criteriaBuilder.equal(root.get("internshipType"), (param.internship())? InternshipType.INTERNSHIP : InternshipType.SEMESTER_OUT))
        ));
    }
}
