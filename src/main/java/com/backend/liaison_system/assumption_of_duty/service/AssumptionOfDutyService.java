package com.backend.liaison_system.assumption_of_duty.service;

import com.backend.liaison_system.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.assumption_of_duty.requests.CreateNewAssumptionOfDuty;
import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import org.springframework.http.ResponseEntity;

public interface AssumptionOfDutyService {
    ResponseEntity<Response<AssumptionOfDuty>> createNewAssumptionOfDuty(String studentID, ConstantRequestParam param, CreateNewAssumptionOfDuty newAssumptionOfDuty);
}
