package com.backend.liaison_system.users.lecturer.repository;

import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.specification.LecturerSpecification;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, String>, JpaSpecificationExecutor<Lecturer> {
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

    /**
     * Searches for {@link Lecturer} entities based on a search key. The search is performed
     * across multiple fields of the {@link Lecturer} entity: company, department, email, faculty,
     * first name, last name, other name, and phone. The search is case-insensitive and matches
     * any substring of the given key in the specified fields.
     *
     * <p>The search key is compared against each of the aforementioned fields using the SQL
     * `LIKE` operator with wildcards on either side of the search key, ensuring that any partial
     * match in these fields will be included in the results.</p>
     *
     * <p>Results are paginated according to the {@link Pageable} parameter provided.</p>
     *
     * @param search the search key used to filter {@link Lecturer} entities. It must not be null.
     * @param pageable the pagination information. Must not be null.
     * @return a {@link Page} of {@link Lecturer} entities that match the search criteria.
     */
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


    default List<Lecturer> findAllLectures(String start, String end) {
        return findAll(
                LecturerSpecification.getAllLecturers(
                        start,
                        end
                )
        );
    }
}
