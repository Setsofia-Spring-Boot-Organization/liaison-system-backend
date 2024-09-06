package com.backend.liaison_system.users.admin;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dto.NewUserRequest;
import com.backend.liaison_system.exception.LiaisonException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Response<?>> uploadStudents(@RequestPart("file") MultipartFile file) {
        return new ResponseEntity<>(adminService.uploadStudents(file), HttpStatus.CREATED);
    }

    @GetMapping("students")
    public ResponseEntity<Response<?>> getStudents() {
        return new ResponseEntity<>(adminService.getStudents(1L), HttpStatus.OK);
    }
}
