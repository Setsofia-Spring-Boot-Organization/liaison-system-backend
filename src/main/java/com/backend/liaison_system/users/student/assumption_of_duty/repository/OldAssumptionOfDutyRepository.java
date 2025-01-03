package com.backend.liaison_system.users.student.assumption_of_duty.repository;

import com.backend.liaison_system.users.student.assumption_of_duty.entities.OldAssumptionOfDuty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OldAssumptionOfDutyRepository extends CrudRepository<OldAssumptionOfDuty, String> {
    List<OldAssumptionOfDuty> findByAssumptionIdOrderByDateCreatedDesc(String assumptionId);
}
