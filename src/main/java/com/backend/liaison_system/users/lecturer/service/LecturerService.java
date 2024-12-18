package com.backend.liaison_system.users.lecturer.service;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.dao.LecturerList;
import com.backend.liaison_system.users.lecturer.dto.NewLecturerRequest;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LecturerService {
    /**
     * This method creates new lecturers based on the provided list of {@link NewLecturerRequest}.
     * It returns a {@link ResponseEntity} containing a {@link Response} with a list of {@link Lecturer} objects.
     *
     * @param request A list of {@link NewLecturerRequest} containing the details for creating new lecturers.
     * @return A {@link ResponseEntity} containing a {@link Response} with a list of created {@link Lecturer} objects.
     */
    ResponseEntity<Response<List<Lecturer>>> createNewLecturer(String adminID, List<NewLecturerRequest> request);

    /**
     * This method retrieves a list of lecturers associated with the specified ID.
     * It returns a {@link ResponseEntity} containing a {@link Response} with a {@link LecturerList} object.
     *
     * @param id The ID used to retrieve the list of lecturers.
     * @return A {@link ResponseEntity} containing a {@link Response} with the {@link LecturerList}.
     */
    ResponseEntity<Response<List<LecturerList>>> getLecturers(String id);

    ResponseEntity<Response<?>> getDashboardData(String id);

    ResponseEntity<Response<?>> superviseStudent(String lecturerId, String studentId);

    ResponseEntity<Response<?>> getStudentsLocation(String lecturerId);

    ResponseEntity<Response<?>> getTopIndustries(String lecturerId, ConstantRequestParam param);
}
