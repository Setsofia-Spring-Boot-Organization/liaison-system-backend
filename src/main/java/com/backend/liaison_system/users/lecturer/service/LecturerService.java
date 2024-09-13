package com.backend.liaison_system.users.lecturer.service;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.lecturer.dto.NewLecturerRequest;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LecturerService {

    ResponseEntity<Response<List<Lecturer>>> createNewLecturer(List<NewLecturerRequest> request);


}
