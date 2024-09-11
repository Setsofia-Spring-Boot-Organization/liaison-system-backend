package com.backend.liaison_system.users.lecturer;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.lecturer.dto.NewLecturerRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LecturerService {
    ResponseEntity<Response<List<Lecturer>>> createNewLecturer(List<NewLecturerRequest> request);
}
