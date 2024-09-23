package com.backend.liaison_system.users.admin.dashboard;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dto.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.users.admin.dashboard.dao.Statistics;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.admin.repository.AdminRepository;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.student.StudentRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DashboardServiceImplTest {

    @Mock
    private AdminUtil adminUtil;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private LecturerRepository lecturerRepository;
    @Mock
    private StudentRepository studentRepository;


    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception{
        if (autoCloseable != null) {
            autoCloseable.close();
        }

        Mockito.reset(adminRepository, lecturerRepository, studentRepository);
    }

    // create dummy lecturers
    List<Lecturer> lecturers() {
        List<Lecturer> lecturers = new ArrayList<>();

        Lecturer lecturer = new Lecturer();

        lecturer.setId(UUID.randomUUID().toString());
        lecturer.setLecturerId(RandomStringUtils.randomAlphanumeric(7).toUpperCase());

        lecturer.setCreatedAt(LocalDateTime.now());
        lecturer.setUpdatedAt(LocalDateTime.now());

        lecturer.setFirstName("Lecturer");
        lecturer.setOtherName("Other");
        lecturer.setLastName("New");
        lecturer.setDp("example-profile-image.com");

        lecturer.setPhone("+233-55-159-963");
        lecturer.setCompany("Lecturer's Company");
        lecturer.setPassword("password");

        lecturer.setRole(UserRoles.LECTURER);

        // create multiple lectures
        for (int i = 0; i < 7; i++) {
            lecturer.setEmail("lecturer" + i + "@email.com");

            lecturers.add(lecturer);
        }


        return lecturers;
    }

    // create a dummy admin
    Admin admin() {
        Admin admin = new Admin();

        admin.setId(UUID.randomUUID().toString());

        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());

        admin.setFirstName("Liaison");
        admin.setLastName("Admin");
        admin.setOtherName("");
        admin.setEmail("admin@ttu.edu.gh");
        admin.setPassword("password");
        admin.setRole(UserRoles.ADMIN);

        return admin;
    }

    @Test
    void getStatistics() {
        // setup
        List<Lecturer> lecturers = lecturers();
        Admin admin = admin();

        when(lecturerRepository.findAll()).thenReturn(lecturers);
        when(adminRepository.findById(admin.getId())).thenReturn(Optional.of(admin));


        ConstantRequestParam constantRequestParam = new ConstantRequestParam(
                InternshipType.INTERNSHIP.name(),
                "2020",
                "2024"
        );

        // do
        ResponseEntity<Response<Statistics>> response = dashboardService.getStatistics(admin.getId(), constantRequestParam);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());

        System.out.println(response.getBody().getData());
    }
}