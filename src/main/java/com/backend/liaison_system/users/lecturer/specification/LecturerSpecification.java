package com.backend.liaison_system.users.lecturer.specification;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.util.UAcademicYear;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class LecturerSpecification {
    public static Specification<Lecturer> getAllLecturers(ConstantRequestParam param) {

        LocalDateTime startDate = UAcademicYear.startOfAcademicYear(param.startOfAcademicYear());
        LocalDateTime endDate = UAcademicYear.endOfAcademicYear(param.endOfAcademicYear());

        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate),
                criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), endDate),
                criteriaBuilder.equal(root.get("semester"), param.semester())
        );
    }
}
