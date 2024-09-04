package com.backend.liaison_system.users.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "liaison/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("students_upload")
    public ResponseEntity<String> uploadStudents(@RequestPart("file") MultipartFile file) {
        return new ResponseEntity<>(adminService.uploadStudents(file), HttpStatus.OK);
    }
}
