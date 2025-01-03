package com.backend.liaison_system.users.admin.controller;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.admin.dao.LecturerData;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.admin.service.AdminService;
import com.backend.liaison_system.users.admin.dto.AdminPageRequest;
import com.backend.liaison_system.dto.NewUserRequest;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.service.AssumptionOfDutyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "liaison/api/v1/admin")
public class AdminController {
    private final AdminService adminService;
    private final AssumptionOfDutyService assumptionOfDutyService;

    public AdminController(AdminService adminService, AssumptionOfDutyService assumptionOfDutyService) {
        this.adminService = adminService;
        this.assumptionOfDutyService = assumptionOfDutyService;
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
            ConstantRequestParam param
    ) {
        return new ResponseEntity<>(adminService.uploadStudents(adminID, file, param), HttpStatus.CREATED);
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

    @GetMapping("/{admin-id}/lecturers/{lecturer-id}")
    public ResponseEntity<Response<LecturerData>> getLecturer(
            @PathVariable("admin-id") String id,
            @PathVariable("lecturer-id") String lecturerId
    ) {
        return adminService.getLecturer(id, lecturerId);
    }

    @GetMapping(path = "/{admin-id}/students/location")
    public ResponseEntity<Response<?>> getAllStudentsLocation(
            @PathVariable("admin-id") String adminId
    ) {
        return adminService.getStudentsLocation(adminId);
    }

    @GetMapping(path = "/{admin-id}/assumption-of-duties/updated")
    public ResponseEntity<Response<List<AssumptionOfDuty>>> getUpdatedAssumptionOfDuties(
            @PathVariable("admin-id") String adminId,
            ConstantRequestParam param,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return adminService.getUpdatedAssumptionOfDuties(adminId, param, page, size);
    }

    @GetMapping(path = "/{admin-id}/assumption-of-duties/{assumption-id}")
    public ResponseEntity<Response<?>> getUpdatedDutyDetails(
            @PathVariable("admin-id") String adminId, 
            @PathVariable("assumption-id") String assumptionId
    ) {
        return assumptionOfDutyService.getUpdatedDutyDetails(adminId, assumptionId);
    }
}
