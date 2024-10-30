package com.backend.liaison_system.zone.service;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.admin.repository.AdminRepository;
import com.backend.liaison_system.zone.dto.NewZone;
import com.backend.liaison_system.zone.entity.Zone;
import com.backend.liaison_system.zone.entity.ZoneLecturers;
import com.backend.liaison_system.zone.repository.ZoneRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ZoneServiceImplTest {

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private ZoneServiceImpl zoneService;

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
        Mockito.reset(zoneRepository, adminRepository);
    }

    // creat a dummy zone data
    NewZone newZone() {
        return new NewZone(
                "New zone",
                "Western region ",
                List.of("Miami"),
                "zoneLeadId",
                List.of(
                        "lecture1",
                        "lecture2",
                        "lecture3",
                        "lecture4"
                )
        );
    }

    // create a dummy admin data
    Admin admin() {
        Admin admin = new Admin();

        admin.setId(UUID.randomUUID().toString());
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        admin.setEmail("john.doe@example.com");
        admin.setFirstName("John");
        admin.setLastName("Doe");
        admin.setOtherName("Michael");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRole(UserRoles.ADMIN);

        return admin;
    }

    @Test
    void createNewZone() {
        NewZone newZone = newZone();
        Admin admin = admin();

        List<NewZone> newZones = new ArrayList<>();

        Zone zone = new Zone();
        zone.setName(newZone.name());
        zone.setRegion(newZone.region());
        zone.setZoneLead(newZone.zoneLead());
        zone.setLecturers(new ZoneLecturers(newZone.lecturerIds()));

        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);

        // operations & assertions
        ResponseEntity<Response<?>> response = zoneService.createNewZone(admin.getId(), newZones);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}