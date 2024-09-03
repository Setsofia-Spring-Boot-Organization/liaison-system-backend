package com.backend.liaison_system.users.lecturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
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
}
