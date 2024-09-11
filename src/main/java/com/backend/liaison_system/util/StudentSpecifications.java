package com.backend.liaison_system.util;

import com.backend.liaison_system.users.student.Student;
import org.springframework.data.jpa.domain.Specification;

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
}
