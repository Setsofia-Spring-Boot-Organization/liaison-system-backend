package com.backend.liaison_system.users.admin.dao;

import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.OldAssumptionOfDuty;

import java.util.Set;

public record UpdatedAssumptionOfDutyData(
        Set<OldAssumptionOfDuty> oldAssumptionOfDuty,
        AssumptionOfDuty updatedAssumptionOfDuty
) { }
