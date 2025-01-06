package com.backend.liaison_system.users.student.assumption_of_duty.service;

import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.requests.CreateNewAssumptionOfDuty;
import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AssumptionOfDutyService {
    ResponseEntity<Response<AssumptionOfDuty>> createNewAssumptionOfDuty(String studentID, ConstantRequestParam param, CreateNewAssumptionOfDuty newAssumptionOfDuty);

    ResponseEntity<Response<?>> updateAssumptionOfDuty(String studentId, String dutyId, CreateNewAssumptionOfDuty duty);

    ResponseEntity<Response<?>> getStudentAssumptionOfDuties(String studentId, ConstantRequestParam param);

    ResponseEntity<Response<?>> getUpdatedDutyDetails(String adminId, String assumptionId);

    ResponseEntity<Response<?>> getAllDuties(String adminId, ConstantRequestParam param, int page, int size);

    /**
     * This method uploads an assumption of duties document for a specified admin.
     *
     * @param adminId The ID of the admin for whom the document is being uploaded.
     * @param param Additional request parameters encapsulated in a {@code ConstantRequestParam} object.
     * @param file The file to be uploaded as a {@code MultipartFile}.
     * @return A {@code ResponseEntity<Response<?>>} containing the response details of the upload operation.
     */
    ResponseEntity<Response<?>> uploadAssumptionOfDuties(String adminId, ConstantRequestParam param, MultipartFile file);
}
