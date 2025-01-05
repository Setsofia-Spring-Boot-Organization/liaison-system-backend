package com.backend.liaison_system.users.lecturer.specification;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.util.UAcademicYear;
import org.springframework.data.jpa.domain.Specification;

public class LecturerSpecification {
    public static Specification<Lecturer> getAllLecturers(ConstantRequestParam param) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("startOfAcademicYear"), UAcademicYear.startOfAcademicYear(param.startOfAcademicYear())),
                criteriaBuilder.lessThanOrEqualTo(root.get("endOfAcademicYear"), UAcademicYear.endOfAcademicYear(param.endOfAcademicYear())),
                criteriaBuilder.equal(root.get("semester"), param.semester())
        );
    }
}
