package com.backend.liaison_system.users.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    /**
     * This method finds a Student entity by its email address.
     *
     * @param email the email address of the student to be found
     * @return an Optional containing the Student entity if found, or an empty Optional if not found
     */
    Optional<Student> findByEmail(String email);
}
