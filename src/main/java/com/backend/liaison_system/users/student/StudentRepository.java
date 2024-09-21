package com.backend.liaison_system.users.student;

import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.users.admin.dto.AdminPageRequest;
import com.backend.liaison_system.util.StudentSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.backend.liaison_system.util.StudentSpecifications.*;

@Repository
public interface StudentRepository extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {
    /**
     * This method finds a Student entity by its email address.
     *
     * @param email the email address of the student to be found
     * @return an Optional containing the Student entity if found, or an empty Optional if not found
     */
    Optional<Student> findByEmail(String email);

    /**
     * This method that queries the data and returns students that match the specified specifications.
     *
     * @param request the AdminPageRequest Object
     * @param pageable this is Pageable that specifies the page
     * @return a page containing the list of matching students
     */
    default Page<Student> findAll(AdminPageRequest request, Pageable pageable, InternshipType internshipType) {
        return findAll(Specification.where(hasName(request.getName()))
                .or(Specification.where(hasLastName(request.getName()))).or(hasOtherName(request.getName()))
                        .and(sameDepartment(request.getDepartment()))
                        .and(sameFaculty(request.getFaculty()))
                        .and(internshipType(internshipType)),
                pageable);
    }

    default List<Student> findAllStudents(String start, String end, String type) {
        return findAll(StudentSpecifications.academicYearAndInternshipType(
                start,
                end,
                type
        ));
    }
}
