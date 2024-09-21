package com.backend.liaison_system.users.admin.controller;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.admin.dao.LecturerData;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.admin.service.AdminService;
import com.backend.liaison_system.users.admin.dto.AdminPageRequest;
import com.backend.liaison_system.dto.NewUserRequest;
import com.backend.liaison_system.exception.LiaisonException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "liaison/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<Response<Admin>> createNewAdmin(
            @Validated @RequestBody NewUserRequest newUserRequest
    ) throws LiaisonException {
        return adminService.creatNewAdmin(newUserRequest);
    }

    @PostMapping("students")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<?>> uploadStudents(@RequestPart("file") MultipartFile file, @RequestPart("internship")boolean internship) {
        return new ResponseEntity<>(adminService.uploadStudents(file, internship), HttpStatus.CREATED);
    }

    @GetMapping("students")
    public ResponseEntity<Response<?>> getStudents(
            AdminPageRequest adminPageRequest
    ) {
        return new ResponseEntity<>(adminService.getStudents(adminPageRequest), HttpStatus.OK);
    }

    @GetMapping("{admin-id}/lecturers")
    public ResponseEntity<Response<?>> getLecturers(
            @PathVariable("admin-id") String id,
            AdminPageRequest adminPageRequest
    ) {
        return adminService.getLecturers(id, adminPageRequest);
    }

    @GetMapping("{admin-id}/lecturers/{lecturer-id}")
    public ResponseEntity<Response<LecturerData>> getLecturer(
            @PathVariable("admin-id") String id,
            @PathVariable("lecturer-id") String lecturerId
    ) {
        return adminService.getLecturer(id, lecturerId);
    }
}
