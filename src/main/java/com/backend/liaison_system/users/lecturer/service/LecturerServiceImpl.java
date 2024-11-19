package com.backend.liaison_system.users.lecturer.service;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.dao.LecturerList;
import com.backend.liaison_system.users.lecturer.dto.NewLecturerRequest;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AdminUtil adminUtil;

    public LecturerServiceImpl(LecturerRepository lecturerRepository, AdminUtil adminUtil) {
        this.lecturerRepository = lecturerRepository;
        this.adminUtil = adminUtil;
    }

    @Override
    public ResponseEntity<Response<List<Lecturer>>> createNewLecturer(String adminID, List<NewLecturerRequest> requests) {
        // verify that the user performing this action is an admin
        adminUtil.verifyUserIsAdmin(adminID);

        // verify if the emails already exist...
        verifyExistingEmails(requests);

        // save the lecturers
        List<Lecturer> lecturers = saveLecturers(requests);

        // save the lecturers
        List<Lecturer> savedLectures = lecturerRepository.saveAll(lecturers);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new Response.Builder<List<Lecturer>>()
                        .status(HttpStatus.CREATED.value())
                        .message("lecturers added successfully!")
                        .data(savedLectures)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response<List<LecturerList>>> getLecturers(String id) {
        adminUtil.verifyUserIsAdmin(id);

        List<Lecturer> lecturers = lecturerRepository.findAll();

        List<LecturerList> lecturerLists = new ArrayList<>();

        for (Lecturer lecturer : lecturers) {
            lecturerLists.add(
                    new LecturerList(
                            lecturer.getId(),
                            lecturer.getLastName() + " " + lecturer.getFirstName(),
                            lecturer.getProfilePictureUrl()
                    )
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new Response.Builder<List<LecturerList>>()
                        .status(HttpStatus.OK.value())
                        .message("lecturers")
                        .data(lecturerLists)
                        .build()
        );
    }



    // helper methods
    /**
     * This method verifies if any of the email addresses in the provided list of NewLecturerRequest objects
     * already exist in the system. If any email is found to be associated with an existing Lecturer,
     * a LiaisonException is thrown with the list of duplicate emails.
     *
     * @param requests a list of NewLecturerRequest objects containing the email addresses to be verified
     * @throws LiaisonException if one or more email addresses already exist in the system
     */
    private void verifyExistingEmails(List<NewLecturerRequest> requests) {

        List<String> emails = new ArrayList<>();
        for (NewLecturerRequest request : requests) {
            Optional<Lecturer> lecturer = lecturerRepository.findByEmail(request.email());

            lecturer.ifPresent(lec -> emails.add(lec.getEmail()));
        }

        if (!emails.isEmpty()) {
            throw new LiaisonException(Error.EMAIL_ALREADY_EXISTS, new Throwable(Message.THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM.label + ": " + emails));
        }
    }

    /**
     * This method processes a list of NewLecturerRequest objects to create and save new Lecturer entities.
     * It initializes each Lecturer with a unique ID, current timestamps, and the provided request data,
     * such as name, email, profile, phone, and company. The password is encoded before being set,
     * and the role is set to LECTURER. All created lecturers are added to a list and returned.
     *
     * @param requests a list of NewLecturerRequest objects containing the data for each new lecturer
     * @return a list of Lecturer entities created from the provided requests
     */
    private List<Lecturer> saveLecturers(List<NewLecturerRequest> requests) {

        List<Lecturer> lecturers = new ArrayList<>();
        for (NewLecturerRequest request : requests) {

            Lecturer lecturer = new Lecturer();

            lecturer.setLecturerId(RandomStringUtils.randomAlphanumeric(7).toUpperCase());
            lecturer.setCreatedAt(LocalDateTime.now());
            lecturer.setUpdatedAt(LocalDateTime.now());

            lecturer.setFirstName(request.firstName());
            lecturer.setOtherName(request.otherName());
            lecturer.setLastName(request.lastName());
            lecturer.setEmail(request.email());
            lecturer.setProfilePictureUrl(request.userProfilePicture());

            lecturer.setPhone(request.phone());
            lecturer.setCompany(request.company());

            lecturer.setFaculty(request.faculty());
            lecturer.setDepartment(request.department());

            String pass = passwordEncoder.encode(request.password());
            lecturer.setPassword(pass);

            lecturer.setRole(UserRoles.LECTURER);

            // add the lecturer to the lecturers' list
            lecturers.add(lecturer);
        }

        return lecturers;
    }
}
