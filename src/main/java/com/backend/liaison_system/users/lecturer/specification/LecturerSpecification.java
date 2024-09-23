package com.backend.liaison_system.users.lecturer.specification;

import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import org.springframework.data.jpa.domain.Specification;

public class LecturerSpecification {

    public static Specification<Lecturer> getAllLecturers() {

        return (root, query, criteriaBuilder) ->
    }
}
