package com.backend.liaison_system.users.admin.controller;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.admin.dao.LecturerData;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.admin.service.AdminService;
import com.backend.liaison_system.users.admin.dto.AdminPageRequest;
import com.backend.liaison_system.dto.NewUserRequest;
import com.backend.liaison_system.exception.LiaisonException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "liaison/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<Response<Admin>> createNewAdmin(
            @Validated @RequestBody NewUserRequest newUserRequest
    ) throws LiaisonException {
        return adminService.creatNewAdmin(newUserRequest);
    }

    @PostMapping("/{admin-id}/students")
    public ResponseEntity<Response<?>> uploadStudents(
            @PathVariable("admin-id") String adminID,
            @RequestPart("file") MultipartFile file,
            boolean internship
    ) {
        return new ResponseEntity<>(adminService.uploadStudents(adminID, file, internship), HttpStatus.CREATED);
    }

    @GetMapping("/{admin-id}/students")
    public ResponseEntity<Response<?>> getStudents(
            @PathVariable("admin-id") String adminID,
            ConstantRequestParam param,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return new ResponseEntity<>(adminService.getStudents(adminID, param, page, size), HttpStatus.OK);
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

    @GetMapping(path = "{admin-id}/students/location")
    public ResponseEntity<Response<?>> getAllStudentsLocation(
            @PathVariable("admin-id") String adminId
    ) {
        return adminService.getStudentsLocation(adminId);
    }
}
