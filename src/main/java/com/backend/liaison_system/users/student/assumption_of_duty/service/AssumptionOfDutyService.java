package com.backend.liaison_system.users.student.assumption_of_duty.service;

import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.requests.CreateNewAssumptionOfDuty;
import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import org.springframework.http.ResponseEntity;

public interface AssumptionOfDutyService {
    ResponseEntity<Response<AssumptionOfDuty>> createNewAssumptionOfDuty(String studentID, ConstantRequestParam param, CreateNewAssumptionOfDuty newAssumptionOfDuty);

    ResponseEntity<Response<?>> updateAssumptionOfDuty(String studentId, String dutyId, CreateNewAssumptionOfDuty duty);

    ResponseEntity<Response<?>> getStudentAssumptionOfDuties(String studentId, ConstantRequestParam param);

    ResponseEntity<Response<?>> getUpdatedDutyDetails(String adminId, String assumptionId);

    ResponseEntity<Response<?>> getAllDuties(String adminId, ConstantRequestParam param, int page, int size);
}
