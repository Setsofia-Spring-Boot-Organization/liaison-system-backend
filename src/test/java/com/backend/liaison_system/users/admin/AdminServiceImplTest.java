package com.backend.liaison_system.users.admin;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dto.NewUserRequest;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.exception.Cause;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private AdminServiceImpl adminService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
        Mockito.reset(adminRepository);
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
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRole(UserRoles.ADMIN);

        return admin;
    }

    // dummy new user request
    NewUserRequest newUserRequest(Admin admin) {
        NewUserRequest request = new NewUserRequest();
        request.setFirstName(admin.getFirstName());
        request.setLastName(admin.getLastName());
        request.setOtherName(admin.getOtherName());
        request.setEmail(admin.getEmail());
        request.setPassword(admin.getPassword());
        request.setRole(admin.getRole());

        return request;
    }

    @Test
    void creatNewAdmin() {
        Admin admin = admin();
        NewUserRequest newUserRequest = newUserRequest(admin);

        when(adminRepository.save(any(Admin.class))).thenReturn(admin);
        when(adminRepository.findByEmail(newUserRequest.getEmail())).thenReturn(Optional.empty());

        // operation:
        ResponseEntity<Response<Admin>> response = adminService.creatNewAdmin(newUserRequest);

        // assertions:
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUserRequest.getFirstName(), response.getBody().getData().getFirstName());
    }

    @Test
    void whenTheUserEmailAlreadyExists_Throw_EMAIL_ALREADY_EXISTS_exception() {
        Admin admin = admin();
        NewUserRequest newUserRequest = newUserRequest(admin);

        when(adminRepository.findByEmail(newUserRequest.getEmail())).thenReturn(Optional.of(admin));

        // operation:
        LiaisonException response = assertThrows(LiaisonException.class, () -> adminService.creatNewAdmin(newUserRequest));

        // assertions:
        assertNotNull(response);
        assertEquals(Error.EMAIL_ALREADY_EXISTS.label, response.getMessage());
    }

    @Test
    void whenSomeFieldsAreEmpty_Throw_REQUIRED_FIELDS_ARE_EMPTY_and_THE_FOLLOWING_FIELDS_ARE_EMPTY_exception() {
        Admin admin = admin();
        admin.setEmail("");
        admin.setPassword("");
        NewUserRequest newUserRequest = newUserRequest(admin);

        // operation:
        LiaisonException response = assertThrows(LiaisonException.class, () -> adminService.validateRequestFields(newUserRequest));

        // assertions:
        assertNotNull(response);
        assertEquals(Error.REQUIRED_FIELDS_ARE_EMPTY.label, response.getMessage());
        assertEquals(Cause.THE_FOLLOWING_FIELDS_ARE_EMPTY.label + "[email, password]", response.getCause().getMessage());
    }

    @Test
    void whenAllFieldsAreEmpty_Throw_REQUIRED_FIELDS_ARE_EMPTY_and_THE_FOLLOWING_FIELDS_ARE_EMPTY_exception() {
        Admin admin = admin();
        admin.setEmail("");
        admin.setFirstName("");
        admin.setLastName("");
        admin.setPassword("");
        NewUserRequest newUserRequest = newUserRequest(admin);

        // operation:
        LiaisonException response = assertThrows(LiaisonException.class, () -> adminService.validateRequestFields(newUserRequest));

        // assertions:
        assertNotNull(response);
        assertEquals(Error.REQUIRED_FIELDS_ARE_EMPTY.label, response.getMessage());
        assertEquals(Cause.THE_FOLLOWING_FIELDS_ARE_EMPTY.label + "[email, firstname, lastname, password]", response.getCause().getMessage());
    }

    @Test
    void whenSomeFieldsAreNull_Throw_REQUIRED_FIELDS_ARE_EMPTY_and_THE_FOLLOWING_FIELDS_ARE_EMPTY_exception() {
        Admin admin = admin();
        admin.setFirstName(null);
        admin.setLastName(null);
        NewUserRequest newUserRequest = newUserRequest(admin);

        // operation:
        LiaisonException response = assertThrows(LiaisonException.class, () -> adminService.validateRequestFields(newUserRequest));

        // assertions:
        assertNotNull(response);
        assertEquals(Error.REQUIRED_FIELDS_ARE_EMPTY.label, response.getMessage());
        assertEquals(Cause.THE_FOLLOWING_FIELDS_ARE_EMPTY.label + "[firstname, lastname]", response.getCause().getMessage());
    }

    @Test
    void whenAllFieldsAreNull_Throw_REQUIRED_FIELDS_ARE_EMPTY_and_THE_FOLLOWING_FIELDS_ARE_EMPTY_exception() {
        Admin admin = admin();
        admin.setEmail(null);
        admin.setFirstName(null);
        admin.setLastName(null);
        admin.setPassword(null);
        NewUserRequest newUserRequest = newUserRequest(admin);

        // operation:
        LiaisonException response = assertThrows(LiaisonException.class, () -> adminService.validateRequestFields(newUserRequest));

        // assertions:
        assertNotNull(response);
        assertEquals(Error.REQUIRED_FIELDS_ARE_EMPTY.label, response.getMessage());
        assertEquals(Cause.THE_FOLLOWING_FIELDS_ARE_EMPTY.label + "[email, firstname, lastname, password]", response.getCause().getMessage());
    }
}