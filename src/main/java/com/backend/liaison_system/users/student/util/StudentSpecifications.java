package com.backend.liaison_system.users.student.util;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.util.UAcademicYear;
import org.springframework.data.jpa.domain.Specification;


/**
 * This is a class that contains various specifications for filtering the Student table
 */
public class StudentSpecifications {

    public static Specification<Student> academicYearAndInternshipType(ConstantRequestParam param) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), UAcademicYear.startOfAcademicYear(param.startOfAcademicYear())),
                criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), UAcademicYear.endOfAcademicYear(param.endOfAcademicYear())),
                criteriaBuilder.equal(root.get("semester"), param.semester()),
                criteriaBuilder.equal(root.get("internshipType"), (param.internship())? InternshipType.INTERNSHIP : InternshipType.SEMESTER_OUT))
        ));
    }


}
