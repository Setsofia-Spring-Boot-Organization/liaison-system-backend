package com.backend.liaison_system.users.lecturer.repository;

import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, String> {
    /**
     * This method finds a Lecturer entity by its email address.
     *
     * @param email the email address of the student to be found
     * @return an Optional containing the Student entity if found, or an empty Optional if not found
     */
    Optional<Lecturer> findByEmail(String email);

    /**
     * This method retrieves a paginated list of all lecturers based on the pagination and filter criteria provided
     * in the AdminPageRequest and Pageable objects. The request allows filtering by fields such as admin ID, name,
     * faculty, and department, while Pageable handles pagination details such as page number and size.
     *
     * @param pageable the Pageable object containing pagination information (page number, page size, etc.)
     * @return a list of lecturers that match the specified filters and pagination settings
     */
    @NonNull
    Page<Lecturer> findAll(@NonNull Pageable pageable);

    /**
     * This method retrieves a list of all lecturers who belong to the specified department.
     *
     * @param department the department name to filter lecturers by
     * @return a list of lecturers who are part of the given department
     */
    List<Lecturer> findAllByDepartment(String department);

    @Query("SELECT lec FROM Lecturer lec WHERE " +
            "LOWER(lec.company) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(lec.department) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(lec.email) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(lec.faculty) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(lec.firstName) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(lec.lastName) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(lec.otherName) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(lec.phone) LIKE LOWER(CONCAT('%', :key, '%'))")
    Page<Lecturer> findLecturerBySearchKey(@Param("key") String search, Pageable pageable);
}
