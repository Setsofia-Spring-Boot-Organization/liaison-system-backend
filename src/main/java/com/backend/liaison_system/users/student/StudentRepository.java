package com.backend.liaison_system.users.student;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.users.student.util.StudentSpecifications;
import com.backend.liaison_system.util.UAcademicYear;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {
    /**
     * This method finds a Student entity by its email address.
     *
     * @param email the email address of the student to be found
     * @return an Optional containing the Student entity if found, or an empty Optional if not found
     */
    Optional<Student> findStudentByStudentEmail(String email);

    /**
     * Finds a student by their email address.
     * This method retrieves a student from the database or storage system based on the provided email address.
     * If a student with the given email exists, an {@link Optional} containing the student will be returned.
     * If no student is found, an empty {@link Optional} will be returned.
     *
     * @param email the email address of the student to be retrieved
     * @return an {@link Optional} containing the student if found, or an empty {@link Optional} if no student is found with the provided email
     * @throws IllegalArgumentException if the provided email is {@code null} or empty
     */
    default Optional<Student> findByEmail(String email) {
        Specification<Student> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("email"), email);
        return findOne(specification);
    };

    /**
     * This method that queries the data and returns students that match the specified specifications.
     *
     * @param param the ConstantRequestParam Object
     * @param pageable this is Pageable that specifies the page
     * @return a page containing the list of matching students
     */
    default Page<Student> findAll(ConstantRequestParam param, Pageable pageable) {
        Specification<Student> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), UAcademicYear.startOfAcademicYear(param.startOfAcademicYear())),
                        criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), UAcademicYear.endOfAcademicYear(param.endOfAcademicYear())),
                        criteriaBuilder.equal(root.get("semester"), param.semester()),
                        criteriaBuilder.equal(root.get("internshipType"), (param.internship())? InternshipType.INTERNSHIP : InternshipType.SEMESTER_OUT)
                );
        return findAll(specification, pageable);
    }

    /**
     * This default method retrieves a list of students based on the provided academic year range and internship type.
     * It uses the {@link StudentSpecifications#academicYearAndInternshipType} to filter the students.
     *
     * @param param The start year of the academic range.
     * @return A list of {@link Student} objects that match the specified criteria.
     */
    default List<Student> findAllStudents(ConstantRequestParam param) {
        return findAll(StudentSpecifications.academicYearAndInternshipType(param));
    }

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS student", nativeQuery = true)
    void dropStudentTable();

    Student findStudentByEmail(String email);
}
