package com.backend.liaison_system.users.lecturer.controller;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.users.dao.LecturerList;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.service.LecturerService;
import com.backend.liaison_system.users.lecturer.dto.NewLecturerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "liaison/api/v1/lecturers")
@RequiredArgsConstructor
public class LecturerController {

    private final LecturerService lecturerService;

    @PostMapping
    public ResponseEntity<Response<List<Lecturer>>> createNewLecturer(
            @RequestBody List<NewLecturerRequest> requests
    ) throws LiaisonException {
        return lecturerService.createNewLecturer(requests);
    }

    @GetMapping(path = "{admin-id}")
    public ResponseEntity<Response<List<Lecturer>>> getLecturers(
            @PathVariable("admin-id") String id
    ) throws LiaisonException {
        return lecturerService.getLecturers(id);
    }
}
