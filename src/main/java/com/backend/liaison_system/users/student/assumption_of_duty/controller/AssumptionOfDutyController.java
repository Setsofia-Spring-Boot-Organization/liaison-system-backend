package com.backend.liaison_system.users.student.assumption_of_duty.controller;

import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.requests.CreateNewAssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.service.AssumptionOfDutyService;
import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "liaison/api/v1/assumption-of-duty")
public class AssumptionOfDutyController {

    private final AssumptionOfDutyService assumptionOfDutyService;

    public AssumptionOfDutyController(AssumptionOfDutyService assumptionOfDutyService) {
        this.assumptionOfDutyService = assumptionOfDutyService;
    }

    @PostMapping(path = "/{student-id}")
    public ResponseEntity<Response<AssumptionOfDuty>> createNewAssumptionOfDuty(
            @PathVariable("student-id") String studentID,
            ConstantRequestParam param,
            @RequestBody CreateNewAssumptionOfDuty newAssumptionOfDuty
    ) {
        return assumptionOfDutyService.createNewAssumptionOfDuty(studentID, param, newAssumptionOfDuty);
    }

    @PutMapping(path = "/{student-id}/update/{duty-id}")
    public ResponseEntity<Response<?>> updateAssumptionOfDuty(
            @PathVariable("student-id") String studentId,
            @PathVariable("duty-id") String dutyId,
            @RequestBody CreateNewAssumptionOfDuty duty
    ) {
        return assumptionOfDutyService.updateAssumptionOfDuty(studentId, dutyId, duty);
    }

    @GetMapping(path = "/{student-id}")
    public ResponseEntity<Response<?>> getStudentAssumptionOfDuties(
            @PathVariable("student-id") String studentId,
            ConstantRequestParam param
    ) {
        return assumptionOfDutyService.getStudentAssumptionOfDuties(studentId, param);
    }
}
