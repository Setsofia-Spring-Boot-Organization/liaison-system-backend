package com.backend.liaison_system.users.lecturer;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.users.lecturer.dto.NewLecturerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
