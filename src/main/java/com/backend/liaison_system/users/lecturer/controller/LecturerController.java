package com.backend.liaison_system.users.lecturer.controller;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.users.dao.LecturerList;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.service.LecturerService;
import com.backend.liaison_system.users.lecturer.dto.NewLecturerRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "liaison/api/v1/lecturers")
public class LecturerController {

    private final LecturerService lecturerService;

    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @PostMapping(path = "{admin-id}")
    public ResponseEntity<Response<List<Lecturer>>> createNewLecturer(
            @RequestBody List<NewLecturerRequest> requests,
            @PathVariable("admin-id") String adminID
    ) throws LiaisonException {
        return lecturerService.createNewLecturer(adminID, requests);
    }

    @GetMapping(path = "{admin-id}")
    public ResponseEntity<Response<List<LecturerList>>> getLecturers(
            @PathVariable("admin-id") String id
    ) throws LiaisonException {
        return lecturerService.getLecturers(id);
    }

    @GetMapping(path = "dashboard/{lecture-id}")
    public ResponseEntity<Response<?>> dashboardStatistics(
            @PathVariable("lecture-id") String id
    ) {
        return lecturerService.getDashboardData(id);
    }
}
