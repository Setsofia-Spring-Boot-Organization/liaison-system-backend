package com.backend.liaison_system.authentication;

import com.backend.liaison_system.authentication.dto.LoginRequest;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dao.data.LoginData;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.user_details.LiaisonUserDetailsService;
import com.backend.liaison_system.users.admin.Admin;
import com.backend.liaison_system.users.admin.AdminRepository;
import com.backend.liaison_system.users.lecturer.LecturerRepository;
import com.backend.liaison_system.users.student.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private LecturerRepository lecturerRepository;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private LiaisonUserDetailsService userDetailsService;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private AutoCloseable autoCloseable;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
        Mockito.reset(adminRepository, lecturerRepository, studentRepository);
    }

    // create dummy admin
    Admin admin() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID().toString());
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        admin.setEmail("admin.email@gmail.com");
        admin.setFirstName("Firstname");
        admin.setLastName("Lastname");
        admin.setOtherName("Other Name");
        admin.setPassword("password");
        admin.setRole(UserRoles.ADMIN);

        return admin;
    }

    // dummy login request
    LoginRequest loginRequest(Admin admin) {

        return new LoginRequest(
                admin.getEmail(),
                admin.getPassword()
        );
    }

    @Test
    void loginUser() {
        Admin admin = admin();
        LoginRequest loginRequest = loginRequest(admin);

        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        adminRepository.save(admin);
        when(adminRepository.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));

        // operations:
        ResponseEntity<Response<LoginData>> response = authenticationService.loginUser(loginRequest);
        System.out.println(response);

        // assertions:
        assertNotNull(response);
        assertNotNull(response.getBody());
    }
}