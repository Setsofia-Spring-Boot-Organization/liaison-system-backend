package com.backend.liaison_system.authentication;

import com.backend.liaison_system.authentication.dto.LoginRequest;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dao.data.LoginData;
import com.backend.liaison_system.enums.UserRoles;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Mock
    private AuthenticationManager authenticationManager;

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
        Mockito.reset(adminRepository, lecturerRepository, studentRepository, authenticationManager);
    }

    Admin admin() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID().toString());
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        admin.setEmail("admin.email@gmail.com");
        admin.setFirstName("Firstname");
        admin.setLastName("Lastname");
        admin.setOtherName("Other Name");
        admin.setPassword(passwordEncoder.encode("password"));  // Encoded password
        admin.setRole(UserRoles.ADMIN);
        return admin;
    }

    LoginRequest loginRequest(Admin admin) {
        return new LoginRequest(
                admin.getEmail(),
                "password"  // Raw password
        );
    }

    @Test
    void loginUser() {
        Admin admin = admin();
        LoginRequest loginRequest = loginRequest(admin);

        when(adminRepository.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mockito.mock(Authentication.class));

//        ResponseEntity<Response<LoginData>> response = authenticationService.loginUser(loginRequest);

//        assertNotNull(response);
//        assertNotNull(response.getBody());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
